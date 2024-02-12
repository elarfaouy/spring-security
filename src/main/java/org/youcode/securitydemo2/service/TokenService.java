package org.youcode.securitydemo2.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.youcode.securitydemo2.domain.entity.Token;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.dto.AuthenticationResponse;
import org.youcode.securitydemo2.repository.TokenRepository;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Value("${application.security.secret-key}")
    private String secretKey;
    @Value("${application.security.token-expiration}")
    private Long tokenExpirationTime;
    @Value("${application.security.refresh-token-expiration}")
    private Long refreshTokenExpirationTime;

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSigningKey() {
        byte[] decodedKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public Token generateRefreshToken(User user) {
        // before generating a new refresh token, we need to revoke all existing refresh tokens for the user
        revokeRefreshTokensByUser(user);

        Token token = new Token();
        token.setRevoked(false);
        token.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationTime));
        token.setUser(user);

        return tokenRepository.save(token);
    }

    public void revokeRefreshTokensByUser(User user) {
        List<Token> tokens = tokenRepository.findByUser(user);
        tokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(tokens);
    }

    public AuthenticationResponse generateNewAccessToken(String refreshToken) {
        Token token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        User user = token.getUser();
        String accessToken = generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenExpiration(extractExpiration(accessToken))
                .build();
    }
}
