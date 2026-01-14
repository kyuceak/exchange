package com.kutay.exchange.modules.ledger.api.dto;

import com.kutay.exchange.shared.model.Asset;

import java.util.UUID;

/* spec --> specification :A structured input object that describes what the caller wants (intent),
without saying how it will be done.
 * */

public record LedgerAccountSpec(UUID walletId, Asset asset, String metadata) {
    public LedgerAccountSpec(UUID walletId, Asset asset) {
        this(walletId, asset, "");
    }
}
