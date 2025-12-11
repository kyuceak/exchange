package com.kutay.exchange.modules.Auth.DTO;

import com.kutay.exchange.modules.Auth.Model.Entity.Role;

public record RegisterRequest(String email,
                              String password,
                              String firstName,
                              String lastName,
                              Role role,
                              String phoneNumber,
                              String address
) {
}
