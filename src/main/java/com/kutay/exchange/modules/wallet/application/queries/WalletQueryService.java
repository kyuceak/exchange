package com.kutay.exchange.modules.wallet.application.queries;

import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.modules.wallet.web.dto.WalletResponse;
import com.kutay.exchange.modules.wallet.web.dto.queries.BalanceInfo;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Wallet query surface for read-model access.
 * Provides balance, wallet and asset availability information.
 */
public interface WalletQueryService {

    /**
     * Get balance for a wallet and asset.
     *
     * @param walletId wallet id (not null)
     * @param asset    asset type (not null)
     * @return Optional BalanceInfo if present
     */
    @NotNull
    Optional<BalanceInfo> getBalance(@NotNull UUID walletId, @NotNull Asset asset);

    /**
     * Get balances for all assets in a wallet.
     *
     * @param walletId wallet id (not null)
     * @return map of Asset to BalanceInfo (never null)
     */
    @NotNull
    Map<Asset, BalanceInfo> getBalances(@NotNull UUID walletId);

    /**
     * Get a wallet by its identifier.
     *
     * @param walletId wallet id (not null)
     * @return WalletResponse for the wallet (not null)
     */
    @NotNull
    WalletResponse getWallet(@NotNull UUID walletId);

    /**
     * Get all wallets associated with a user.
     *
     * @param userId user id (not null)
     * @return list of WalletResponse (never null)
     */
    @NotNull
    List<WalletResponse> getWallets(@NotNull UUID userId);

    /**
     * Get the amount available to spend for a given asset in a wallet.
     *
     * @param walletId wallet id (not null)
     * @param asset    asset type (not null)
     * @return available amount as BigDecimal (never null)
     */
    @NotNull
    BigDecimal getAvailable(@NotNull UUID walletId, @NotNull Asset asset);
}
