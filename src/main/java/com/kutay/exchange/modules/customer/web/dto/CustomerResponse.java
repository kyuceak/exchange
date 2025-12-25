package com.kutay.exchange.modules.customer.web.dto;

public record CustomerResponse(Long id,
                               String firstName,
                               String lastName,
                               String phoneNumber,
                               String address) {
}
