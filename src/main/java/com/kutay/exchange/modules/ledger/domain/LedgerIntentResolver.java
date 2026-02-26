package com.kutay.exchange.modules.ledger.domain;

import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.internal.account.AccountCodeGenerator;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.shared.contracts.EntryDirection;
import com.kutay.exchange.shared.contracts.EntryLayer;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerIntentResolver {
    private final AccountRepository accountRepository;

    public List<InternalTransaction.EntryLine> resolve(LedgerIntent intent) {
        return switch (intent.transactionType()) {
            case DEPOSIT -> buildDepositEntries(intent);
            case WITHDRAWAL -> buildWithdrawalEntries(intent);
            case TRADE_BUY -> null;
            case TRADE_SELL -> null;
            case TRADE_FEE -> null;
            case TRANSFER_IN -> null;
            case TRANSFER_OUT -> null;
            case REFUND -> null;
            case CORRECTION -> null;
        };
    }

    private List<InternalTransaction.EntryLine> buildDepositEntries(LedgerIntent intent) {
        UUID userAccountId = accountRepository
                .findByWalletIdAndAsset(intent.walletId(), intent.asset())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account not found for walletId=" +
                                intent.walletId() + ", asset=" + intent.asset()))
                .getId();
        UUID systemAccountId =
                resolveSystemAccountId(intent.asset());
        return List.of(new InternalTransaction.EntryLine(systemAccountId,
                        intent.amount(), EntryDirection.DEBIT, EntryLayer.AVAILABLE),
                new InternalTransaction.EntryLine(userAccountId,
                        intent.amount(), EntryDirection.CREDIT, EntryLayer.AVAILABLE));

    }

    private List<InternalTransaction.EntryLine> buildWithdrawalEntries(LedgerIntent intent) {
        UUID userAccountId = accountRepository.
                findByWalletIdAndAsset(intent.walletId(), intent.asset())
                .orElseThrow(() -> new EntityNotFoundException("Account not found for walletId=" +
                        intent.walletId() + ", asset=" + intent.asset())).getId();
        UUID systemAccountId = resolveSystemAccountId(intent.asset());


        return List.of(new InternalTransaction.EntryLine(userAccountId,
                        intent.amount(), EntryDirection.DEBIT,
                        EntryLayer.AVAILABLE),
                new InternalTransaction.EntryLine(systemAccountId,
                        intent.amount(), EntryDirection.CREDIT,
                        EntryLayer.AVAILABLE)
        );
    }

    private UUID resolveSystemAccountId(Asset asset) {
        String code =
                AccountCodeGenerator.generateSystem(
                        AccountType.ASSET, asset,
                        SystemAccountPurpose.CRYPTO_HOLDINGS
                );
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new
                        EntityNotFoundException("System account not found: " + code))
                .getId();
    }
}
