package org.youcode.securitydemo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.youcode.securitydemo2.domain.entity.Token;
import org.youcode.securitydemo2.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);

    Optional<Token> findByToken(String token);

    Optional<Token> findByUuid(UUID uuid);
}
