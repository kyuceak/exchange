package com.kutay.exchange.modules.customer.web.dto;

public record CustomerRequest(String nationalId, String firstName, String lastName, String phoneNumber,
                              String address) {
}
