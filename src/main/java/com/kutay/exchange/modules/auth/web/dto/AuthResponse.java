package com.kutay.exchange.modules.auth.web.dto;

import com.kutay.exchange.modules.auth.domain.model.Role;

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
