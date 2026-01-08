package com.kutay.exchange.modules.ledger.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(@NotNull UUID walletId,
                                   @NotNull String code

) {
}
