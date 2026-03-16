package com.kutay.exchange.modules.ledger.domain;

import com.kutay.exchange.modules.ledger.api.dto.InternalTransaction;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.internal.account.AccountCodeGenerator;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.contracts.Asset;
import com.kutay.exchange.shared.contracts.EntryDirection;
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
            case RESERVE -> buildReserveEntries(intent);
            case RELEASE -> buildReleaseEntries(intent);
            case SETTLEMENT_WITHDRAWAL -> buildSettlementWithdrawal(intent);
            case SETTLEMENT_DEPOSIT -> buildSettlementDeposit(intent);
        };
    }

    // SETTLED(-) --> PENDING_DEBIT(+)
    private List<InternalTransaction.EntryLine> buildReserveEntries(LedgerIntent intent) {
        UUID settledAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.SETTLED);
        UUID pendingDebitAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_DEBIT);
        return
                List.of(
                        new InternalTransaction.EntryLine(settledAccountId, intent.amount(), EntryDirection.DEBIT),
                        new InternalTransaction.EntryLine(pendingDebitAccountId, intent.amount(), EntryDirection.CREDIT)
                );
    }

    // PENDING_DEBIT --> SETTLED
    private List<InternalTransaction.EntryLine> buildReleaseEntries(LedgerIntent intent) {
        UUID pendingDebitAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_DEBIT);
        UUID settledAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.SETTLED);
        return List.of(
                new InternalTransaction.EntryLine(settledAccountId, intent.amount(), EntryDirection.CREDIT),
                new InternalTransaction.EntryLine(pendingDebitAccountId, intent.amount(), EntryDirection.DEBIT)
        );
    }

    // PENDING_DEBIT(-) --> SYSTEM.CRYPTO_HOLDINGS(-)
    private List<InternalTransaction.EntryLine> buildSettlementWithdrawal(LedgerIntent intent) {
        UUID pendingDebitAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_DEBIT);
        UUID systemHoldingsId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.SETTLED);
        return List.of(
                new InternalTransaction.EntryLine(pendingDebitAccountId, intent.amount(), EntryDirection.DEBIT),
                new InternalTransaction.EntryLine(systemHoldingsId, intent.amount(), EntryDirection.CREDIT)
        );
    }

    // PENDING_CREDIT(+)  +  SYSTEM.CRYPTO_HOLDINGS.PENDING_DEBIT(+)
    private List<InternalTransaction.EntryLine> buildDepositEntries(LedgerIntent intent) {
        UUID pendingCreditAccountId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_CREDIT);
        UUID systemAccountId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.PENDING_DEBIT);
        return List.of(
                new InternalTransaction.EntryLine(systemAccountId, intent.amount(), EntryDirection.DEBIT),
                new InternalTransaction.EntryLine(pendingCreditAccountId, intent.amount(), EntryDirection.CREDIT)
        );
    }

    // add user.PENDING_CREDIT(-) --> user.SETTLED(+)
    // and system.CRYPTO_HOLDINGS.PENDING_DEBIT(-) --> system.CRYPTO_HOLDINGS.SETTLED(+)
    private List<InternalTransaction.EntryLine> buildSettlementDeposit(LedgerIntent intent) {
        UUID userPendingCreditId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_CREDIT);
        UUID userSettledId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.SETTLED);
        UUID systemPendingDebitId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.PENDING_DEBIT);
        UUID systemSettledId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.SETTLED);
        return List.of(
                new InternalTransaction.EntryLine(userPendingCreditId, intent.amount(), EntryDirection.DEBIT),
                new InternalTransaction.EntryLine(userSettledId, intent.amount(), EntryDirection.CREDIT),
                new InternalTransaction.EntryLine(systemPendingDebitId, intent.amount(), EntryDirection.CREDIT),
                new InternalTransaction.EntryLine(systemSettledId, intent.amount(), EntryDirection.DEBIT)
        );
    }

    // add USER.SETTLED(-)  ,USER.PENDING_DEBIT(+)
    // and SYSTEM.CRYPTO_HOLDINGS.SETTLED(-), SYSTEM.CRYPTO_HOLDINGS.PENDING_CREDIT(+)
    private List<InternalTransaction.EntryLine> buildWithdrawalEntries(LedgerIntent intent) {
        UUID userPendingDebitId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.PENDING_DEBIT);
        UUID userSettledId = resolveUserAccountId(intent.walletId(), intent.asset(), AccountState.SETTLED);
        UUID systemPendingCreditId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.PENDING_CREDIT);
        UUID systemSettledId = resolveSystemAccountId(intent.asset(), SystemAccountPurpose.CRYPTO_HOLDINGS, AccountState.SETTLED);
        return List.of(
                new InternalTransaction.EntryLine(userSettledId, intent.amount(), EntryDirection.DEBIT),
                new InternalTransaction.EntryLine(userPendingDebitId, intent.amount(), EntryDirection.CREDIT),
                new InternalTransaction.EntryLine(systemSettledId, intent.amount(), EntryDirection.CREDIT),
                new InternalTransaction.EntryLine(systemPendingCreditId, intent.amount(), EntryDirection.DEBIT)
        );
    }

    private UUID resolveUserAccountId(UUID walletId, Asset asset, AccountState state) {
        return accountRepository.findByWalletIdAndAssetAndState(walletId, asset, state)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account not found for walletId=" + walletId + ", asset=" + asset))
                .getId();
    }

    private UUID resolveSystemAccountId(Asset asset, SystemAccountPurpose accountPurpose, AccountState state) {
        AccountType accType = switch (accountPurpose) {
            case CRYPTO_HOLDINGS -> AccountType.ASSET;
            case TRADING_FEES, WITHDRAWAL_FEES -> AccountType.REVENUE;
            case ADJUSTMENTS -> AccountType.EXPENSE;
            case PENDING_PAYMENTS -> AccountType.LIABILITY;
        };

        String code =
                AccountCodeGenerator.generateSystem(
                        accType,
                        asset,
                        accountPurpose,
                        state);
        return accountRepository.findByCode(code)
                .orElseThrow(() -> new
                        EntityNotFoundException("System account not found: " + code))
                .getId();
    }
}
