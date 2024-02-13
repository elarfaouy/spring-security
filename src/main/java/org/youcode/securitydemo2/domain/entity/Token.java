package org.youcode.securitydemo2.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean revoked;
    private Instant expiryDate;

    @Column(unique = true)
    private UUID uuid;

    @Column(unique = true)
    private String token;

    @ManyToOne
    private User user;
}
