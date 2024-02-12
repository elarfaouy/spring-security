package org.youcode.securitydemo2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AuthenticationResponse {
    @JsonProperty("access-token")
    private String accessToken;
    @JsonProperty("refresh-token")
    private String refreshToken;
    @JsonProperty("token-expiration")
    private Date tokenExpiration;
}
