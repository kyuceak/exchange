package com.kutay.exchange.modules.auth.web.dto;

import com.kutay.exchange.modules.auth.domain.model.Role;

public record RegisterRequest(String email,
                              String password,
                              String firstName,
                              String lastName,
                              Role role,
                              String phoneNumber,
                              String address
) {
}
