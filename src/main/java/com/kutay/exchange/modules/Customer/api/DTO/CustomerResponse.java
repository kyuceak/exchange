package com.kutay.exchange.modules.Customer.api.DTO;

public record CustomerResponse(Long id,
                               String firstName,
                               String lastName,
                               String phoneNumber,
                               String address) {
}
