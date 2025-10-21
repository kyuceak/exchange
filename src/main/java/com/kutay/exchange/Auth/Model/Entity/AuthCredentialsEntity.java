package com.kutay.exchange.Auth.Model.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
public class AuthCredentialsEntity {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private UUID customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Column()
    private boolean emailVerified = false;

    @Column()
    @Enumerated(EnumType.STRING) // db doesn't know how to store ENUM, specify it
    private UserStatus status = UserStatus.ACTIVE; // check later for KYC

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column()
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime lastLoginAt;
}
