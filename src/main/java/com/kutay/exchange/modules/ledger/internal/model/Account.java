package com.kutay.exchange.modules.ledger.internal.model;

import com.kutay.exchange.shared.model.AbstractBaseEntity;
import com.kutay.exchange.shared.model.Asset;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_code",
                        columnNames = {"code"}
                )
        },
        indexes = {
                @Index(name = "idx_account_wallet_id", columnList = "walletId"),
                @Index(name = "idx_account_asset", columnList = "asset"),
                @Index(name = "idx_account_currency", columnList = "currency")
        })
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "wallet_id", nullable = false, updatable = false)
    private UUID walletId; // identity reference

    @Column(nullable = false, updatable = false)
    private String code;  // e.g., "ASSETS:BANK:USD", "LIABILITIES:USER_123:BTC"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Asset asset;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private transient Map<String, Object> metadata;
}
