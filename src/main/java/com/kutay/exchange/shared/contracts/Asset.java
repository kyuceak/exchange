package com.kutay.exchange.shared.contracts;

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
    TRY;


    public static Asset fromCurrencyCode(String currency) {
        return switch (currency) {
            case "TRY" -> Asset.TRY;
            default -> throw new IllegalStateException("Unexpected value: " + currency);
        };
    }
}
