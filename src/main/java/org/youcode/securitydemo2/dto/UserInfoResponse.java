package org.youcode.securitydemo2.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoResponse {
    private String username;
    private String password;
    private String role;
    private List<String> permissions;
}
