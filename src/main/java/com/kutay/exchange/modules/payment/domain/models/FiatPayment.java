package com.kutay.exchange.modules.payment.domain.models;

import com.kutay.exchange.modules.payment.domain.models.enums.Direction;
import com.kutay.exchange.modules.payment.domain.models.enums.PaymentMethod;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import com.kutay.exchange.shared.model.Asset;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_fiat_bank_ref", columnNames = {"bank_ref"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FiatPayment extends Payment {
    @Id
    private UUID id;

    //    @MapsId // child entity lifecycle is identical to the parents. child is not optional
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "payment_id", nullable = false)
//    private Payment payment;

    private String iban;

    @Column(nullable = false, updatable = false)
    private String bankRef;

    private String swift;

    private String senderName;

    private String receiverAccount;

    private FiatPayment(UUID walletId,
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
    }


    public static FiatPayment localDeposit(UUID walletId,
                                           Asset asset,
                                           BigDecimal amount,
                                           String iban,
                                           String bankRef,
                                           String receiverAccount,
                                           String senderName) {
        return new FiatPayment(walletId,
                Direction.DEPOSIT,
                asset,
                amount,
                iban,
                bankRef,
                receiverAccount,
                senderName,
                null);
    }

    public static FiatPayment localWithdraw(UUID walletId,
                                            Asset asset,
                                            BigDecimal amount,
                                            String iban,
                                            String bankRef,
                                            String receiverAccount,
                                            String senderName) {
        return new FiatPayment(walletId,
                Direction.WITHDRAW,
                asset,
                amount,
                iban,
                bankRef,
                receiverAccount,
                senderName,
                null);
    }

    public static FiatPayment internationalDeposit(UUID walletId,
                                                   Asset asset,
                                                   BigDecimal amount,
                                                   String iban,
                                                   String bankRef,
                                                   String receiverAccount,
                                                   String senderName,
                                                   String swift) {
        return new FiatPayment(walletId,
                Direction.DEPOSIT,
                asset,
                amount,
                iban,
                bankRef,
                receiverAccount,
                senderName,
                swift);
    }

    public static FiatPayment internationalWithdraw(UUID walletId,
                                                    Asset asset,
                                                    BigDecimal amount,
                                                    String iban,
                                                    String bankRef,
                                                    String receiverAccount,
                                                    String senderName,
                                                    String swift) {
        return new FiatPayment(walletId,
                Direction.WITHDRAW,
                asset,
                amount,
                iban,
                bankRef,
                receiverAccount,
                senderName,
                swift);
    }
}
