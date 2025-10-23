package com.kutay.exchange.Auth.DTO;

import com.kutay.exchange.Auth.Model.Entity.Role;

public record RegisterRequest(String email,
                              String password,
                              String firstName,
                              String lastName,
                              Role role,
                              String phoneNumber,
                              String address
                              ) {
}
