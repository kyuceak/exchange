package com.kutay.exchange.modules.payment.domain.models;


import com.kutay.exchange.modules.payment.domain.models.enums.BlockchainNetwork;
import com.kutay.exchange.modules.payment.domain.models.enums.CryptoState;
import com.kutay.exchange.shared.contracts.Direction;
import com.kutay.exchange.modules.payment.domain.models.enums.PaymentMethod;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_network_transaction_hash", columnNames = {"network", "tx_hash"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CryptoPayment extends Payment {
    @Column(length = 128)
    private String txHash; // blockchain transaction hash, unique identifier on the blockchain

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BlockchainNetwork network;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CryptoState state;

    @Column(nullable = false)
    private int confirmations; // how many block have been mined on top of this transaction so far

    // for bitcoin 6 is consired as enough
    @Column(nullable = false)
    private int requiredConfirmations; // the threshold to consider the transaction final and safe

    private Long blockHeight;

    private CryptoPayment(UUID walletId,
                          Direction direction,
                          Asset asset,
                          BigDecimal amount,
                          BlockchainNetwork network,
                          String txHash) {
        super(walletId, direction, asset, PaymentMethod.CRYPTO, amount);
        this.txHash = txHash;
        this.network = network;
        this.requiredConfirmations = network.getRequiredConfirmations();
        this.confirmations = 0;
    }

    // deposit — txHash known at creation (comes fromwebhook)
    public static CryptoPayment deposit(UUID walletId,
                                        Asset asset,
                                        BigDecimal amount,
                                        BlockchainNetwork network,
                                        String txHash) {
        return new CryptoPayment(walletId,
                Direction.DEPOSIT, asset, amount, network, txHash);
    }

    // withdrawal — txHash unknown at creation, set later after broadcast
    public static CryptoPayment withdraw(UUID walletId,
                                         Asset asset,
                                         BigDecimal amount,
                                         BlockchainNetwork network) {
        return new CryptoPayment(walletId,
                Direction.WITHDRAW, asset, amount, network, null);
    }

    public void setTxHash(String txHash) {
        Objects.requireNonNull(txHash);
        this.txHash = txHash;
    }

    public void startConfirming() {
        if (state != CryptoState.DETECTED) {
            throw new IllegalStateException("Crypto payment already confirming, id: " + getId());
        }
        this.state = CryptoState.CONFIRMING;
    }

    public void finalizePayment(long blockHeight) {
        if (state != CryptoState.CONFIRMING) {
            throw new IllegalStateException("Cannot finalize from state: " + state + " id: " + getId());
        }
        this.blockHeight = blockHeight;
        this.state = CryptoState.FINALIZED;
    }

    public void reorgDrop() {
        this.state = CryptoState.REORG_DROPPED;
    }

    public void fail() {
        this.state = CryptoState.FAILED;
    }
}
