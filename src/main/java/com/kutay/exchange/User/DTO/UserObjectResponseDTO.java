package com.kutay.exchange.User.DTO;

public record UserObjectResponseDTO(String email,
                                    String username,
                                    String status,
                                    String emailVerified) {
}
