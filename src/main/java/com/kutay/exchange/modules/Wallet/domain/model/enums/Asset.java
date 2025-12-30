package com.kutay.exchange.modules.Wallet.domain.model.enums;

/**
 * Supported cryptocurrency and fiat assets.
 * Shared across modules (Wallet, Ledger, Trading, etc.)
 */
public enum Asset {
    // Cryptocurrencies
    BTC,
    ETH,
    USDT,
    USDC,

    // Fiat (if needed)
    USD,
    EUR,
    TRY
}
