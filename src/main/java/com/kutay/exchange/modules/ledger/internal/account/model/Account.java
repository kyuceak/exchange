package com.kutay.exchange.modules.ledger.internal.account.model;

import com.kutay.exchange.modules.ledger.internal.account.AccountCodeGenerator;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.model.AbstractBaseEntity;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_wallet_asset_state",
                        columnNames = {"wallet_id", "asset", "state"}
                ),
                @UniqueConstraint(
                        name = "uk_account_code",
                        columnNames = {"code"}
                ), // later think about + walletId and Asset is unique
        },
        indexes = {
                @Index(name = "idx_account_wallet_id", columnList = "walletId"),
                @Index(name = "idx_account_asset", columnList = "asset"),
                @Index(name = "idx_account_code", columnList = "code"),
                @Index(name = "idx_account_balance", columnList = "balance")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends AbstractBaseEntity {
    private Account(UUID walletId,
                    Asset asset,
                    AccountType accountType,
                    AccountScope scope,
                    SystemAccountPurpose purpose,
                    String metadata,
                    AccountState state
    ) {
        this.walletId = walletId;
        this.asset = asset;
        this.accountType = accountType;
        this.accountScope = scope;
        this.code = switch (scope) {
            case SYSTEM -> AccountCodeGenerator.generateSystem(accountType, asset, purpose, state);
            case USER -> AccountCodeGenerator.generateUser(accountType, asset, walletId, state);
        };
        this.metadata = metadata;
        this.balance = BigDecimal.ZERO;
        this.state = state;
    }

    public static Account createUserAccount(UUID walletId, Asset asset, String metadata, AccountState state) {
        Objects.requireNonNull(walletId, "walletId is required for user accounts");
        Objects.requireNonNull(asset, "asset is required");
        Objects.requireNonNull(state, "state is required");
        return new Account(walletId, asset, AccountType.LIABILITY, AccountScope.USER, null, metadata, state);
    }

    public static Account createSystemAccount(Asset asset,
                                              AccountType accountType,
                                              SystemAccountPurpose purpose,
                                              String metadata,
                                              AccountState state) {
        Objects.requireNonNull(asset, "asset is required");
        Objects.requireNonNull(accountType, "accountType is required");
        Objects.requireNonNull(purpose, "purpose is required for system accounts");
        Objects.requireNonNull(state, "state is required");
        return new Account(null, asset, accountType, AccountScope.SYSTEM, purpose, metadata, state);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private AccountState state;

    @Column(name = "wallet_id", updatable = false)
    private UUID walletId; // identity reference

    @Column(nullable = false, updatable = false)
    private String code;  // e.g., "ASSETS:BANK:USD", "LIABILITIES:USER_123:BTC" --> invariant

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccountType accountType; // asset,liability,revenue ...

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccountScope accountScope; // USER, SYSTEM...

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; //  Map<String,Object> --> Object has not serializable guarantee

    public void addToBalance(BigDecimal signedAmount) {
        balance = balance.add(signedAmount);
    }
}
