package com.kutay.exchange.modules.ledger.web.dto;

import com.kutay.exchange.shared.model.Asset;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(@NotNull UUID walletId,
                                   @NotNull Asset asset,
                                   String metadata
) {
}
