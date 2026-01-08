package com.kutay.exchange.modules.ledger.internal.account;

import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.model.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LedgerAccountFactory {
    private final AccountRepository accountRepository;

    @Transactional
    public UUID createUserAccount(LedgerAccountSpec ledgerAccountSpec) {
        // 1. first check if it exists if yes return
        Optional<Account> account = accountRepository
                .findByWalletIdAndAssetAndAccountTypeAndAccountScope(
                        ledgerAccountSpec.walletId(),
                        ledgerAccountSpec.asset(),
                        AccountType.LIABILITY,
                        AccountScope.USER
                );
        if (account.isPresent()) {
            return account.get().getId();
        }
        // 2. if it doesnt exist create it in the DB. (DB constraint will prevent race condition)
        try {
            return accountRepository.save(new Account(ledgerAccountSpec.walletId(),
                    ledgerAccountSpec.asset(),
                    AccountType.LIABILITY,
                    AccountScope.USER,
                    null,
                    ledgerAccountSpec.metadata()
            )).getId();

        } catch (DataIntegrityViolationException exception) {
            // race condition happened it will exist for sure now.
            return accountRepository
                    .findByWalletIdAndAssetAndAccountTypeAndAccountScope(
                            ledgerAccountSpec.walletId(),
                            ledgerAccountSpec.asset(),
                            AccountType.LIABILITY,
                            AccountScope.USER
                    ).orElseThrow().getId();
        }
    }

    @Transactional
    public UUID createSystemAccount(Asset asset, AccountType accountType, SystemAccountPurpose purpose) {
        UUID systemWalletId = UUID.fromString("00000000-0000-0000-0000-000000000000"); // mock walletId for system
        String code = AccountCodeGenerator.generateSystem(accountType, asset, purpose);
        // 1. first check if it exists if yes return
        Optional<Account> account = accountRepository.findByCode(code);
        if (account.isPresent()) {
            return account.get().getId();
        }
        // 2. if it doesnt exist create it in the DB. (DB constraint will prevent race condition)
        try {
            return accountRepository.save(new Account(systemWalletId,
                    asset,
                    accountType,
                    AccountScope.SYSTEM,
                    purpose,
                    null
            )).getId();

        } catch (DataIntegrityViolationException exception) {
            // race condition happened it will exist for sure now.
            return accountRepository
                    .findByCode(code)
                    .orElseThrow().getId();
        }
    }
}
