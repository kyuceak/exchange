package com.kutay.exchange.modules.payment.domain.models;

import com.kutay.exchange.modules.payment.domain.models.enums.FiatState;
import com.kutay.exchange.shared.contracts.Direction;
import com.kutay.exchange.modules.payment.domain.models.enums.PaymentMethod;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        indexes = {@Index(name = "idx_bank_ref", columnList = "bank_ref")},
        uniqueConstraints = @UniqueConstraint(name = BankTransfer.UK_BANK_REF, columnNames = {"bank_ref"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankTransfer extends Payment {
    public static final String UK_BANK_REF = "uk_fiat_bank_ref";

    private String iban;

    @Column(nullable = false, updatable = false)
    private String bankRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FiatState state;

    private String swift;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String receiverAccount;

    private BankTransfer(UUID walletId,
                         Direction direction,
                         Asset asset,
                         BigDecimal amount,
                         String iban,
                         String bankRef,
                         String receiverAccount,
                         String senderName,
                         String swift) {
        super(walletId, direction, asset,
                PaymentMethod.FIAT, amount);
        this.iban = iban;
        this.bankRef = bankRef;
        this.receiverAccount = receiverAccount;
        this.senderName = senderName;
        this.swift = swift;
        this.state = FiatState.CREATED;
    }


    public static BankTransfer localDeposit(UUID walletId,
                                            Asset asset,
                                            BigDecimal amount,
                                            String iban,
                                            String bankRef,
                                            String receiverAccount,
                                            String senderName) {
        return new BankTransfer(
                walletId,
                Direction.DEPOSIT,
                asset,
                amount,
                iban,
                bankRef,
                receiverAccount,
                senderName,
                null);
    }

    public static BankTransfer localWithdraw(UUID walletId,
                                             Asset asset,
                                             BigDecimal amount,
                                             String senderIban,
                                             String bankRef,
                                             String receivingIban,
                                             String senderName) {
        return new BankTransfer(
                walletId,
                Direction.WITHDRAW,
                asset,
                amount,
                senderIban,
                bankRef,
                receivingIban,
                senderName,
                null);
    }

    public static BankTransfer internationalDeposit(UUID walletId,
                                                    Asset asset,
                                                    BigDecimal amount,
                                                    String senderIban,
                                                    String bankRef,
                                                    String receivingIban,
                                                    String senderName,
                                                    String swift) {
        return new BankTransfer(
                walletId,
                Direction.DEPOSIT,
                asset,
                amount,
                senderIban,
                bankRef,
                receivingIban,
                senderName,
                swift);
    }

    public static BankTransfer internationalWithdraw(UUID walletId,
                                                     Asset asset,
                                                     BigDecimal amount,
                                                     String senderIban,
                                                     String bankRef,
                                                     String receivingIban,
                                                     String senderName,
                                                     String swift) {
        return new BankTransfer(
                walletId,
                Direction.WITHDRAW,
                asset,
                amount,
                senderIban,
                bankRef,
                receivingIban,
                senderName,
                swift);
    }

    public void sendToProvider() {
        if (state != FiatState.CREATED) {
            throw new IllegalStateException("Fiat payment already started, id: " + getId());
        }
        this.state = FiatState.PENDING_PROVIDER;
    }

    public void authorize() {
        if (state != FiatState.PENDING_PROVIDER) {
            throw new IllegalStateException("Cannot authorize from state: " + state + " id: " + getId());
        }
        this.state = FiatState.AUTHORIZED;
    }

    public void settle() {
        if (state != FiatState.AUTHORIZED) {
            throw new IllegalStateException("Cannot settle from state: " + state + " id: " + getId());
        }
        this.state = FiatState.SETTLED;
    }

    public void decline() {
        this.state = FiatState.DECLINED;
    }
}
