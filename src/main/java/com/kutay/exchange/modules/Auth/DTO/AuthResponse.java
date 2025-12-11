package com.kutay.exchange.modules.Auth.DTO;

import com.kutay.exchange.modules.Auth.Model.Entity.Role;

public record AuthResponse(
        Long id,
        String email,
        boolean emailVerified,
        String firstName,
        String lastName,
        Role role,
        String phoneNumber,
        String address
) {
}
