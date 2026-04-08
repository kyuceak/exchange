package com.kutay.exchange.modules.payment.infrastracture.external;

import jakarta.validation.constraints.NotNull;

public record BankApiResponse(@NotNull String bankRef,
                              @NotNull String status) {
}
