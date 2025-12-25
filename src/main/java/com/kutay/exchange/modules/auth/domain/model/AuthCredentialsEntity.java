package com.kutay.exchange.modules.auth.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
public class AuthCredentialsEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "credentials_id_generator")
    @SequenceGenerator(name = "credentials_id_generator",
            sequenceName = "credentials_id_seq"
    )
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private Long customerId;

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

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime lastLoginAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
