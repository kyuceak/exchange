package com.kutay.exchange.modules.auth.web.dto;

import com.kutay.exchange.modules.auth.domain.model.Role;

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
