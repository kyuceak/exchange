package com.kutay.exchange.modules.ledger.internal.account.model;

import com.kutay.exchange.modules.ledger.internal.account.AccountCodeGenerator;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import com.kutay.exchange.shared.model.Asset;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_code",
                        columnNames = {"code"}
                ),
        },
        indexes = {
                @Index(name = "idx_account_wallet_id", columnList = "walletId"),
                @Index(name = "idx_account_asset", columnList = "asset"),
                @Index(name = "idx_account_code", columnList = "code")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Account(UUID walletId, Asset asset, AccountType accountType, AccountScope scope, SystemAccountPurpose purpose, String metadata) {
        this.walletId = walletId;
        this.asset = asset;
        this.accountType = accountType;
        this.accountScope = scope;
        this.code = switch (scope) {
            case SYSTEM -> AccountCodeGenerator.generateSystem(accountType, asset, purpose);
            case USER -> AccountCodeGenerator.generateUser(accountType, asset, walletId);
        };
        this.metadata = metadata;
    }

    @Column(name = "wallet_id", nullable = false, updatable = false)
    private UUID walletId; // identity reference

    @Column(nullable = false, updatable = false)
    private String code;  // e.g., "ASSETS:BANK:USD", "LIABILITIES:USER_123:BTC" --> invariant

    @Column(nullable = false, updatable = false)
    private AccountType accountType; // asset,liability,revenue ...

    @Column(nullable = false, updatable = false)
    private AccountScope accountScope; // USER, SYSTEM...

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; //  Map<String,Object> --> Object has not serializable guarantee
}
