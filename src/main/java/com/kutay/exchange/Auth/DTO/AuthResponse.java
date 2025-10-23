package com.kutay.exchange.Auth.DTO;

import com.kutay.exchange.Auth.Model.Entity.Role;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String email,
        boolean emailVerified,
        String firstName,
        String lastName,
        Role role,
        String phoneNumber,
        String address
                           ) {
}
