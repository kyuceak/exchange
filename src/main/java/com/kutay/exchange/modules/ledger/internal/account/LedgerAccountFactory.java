package com.kutay.exchange.modules.ledger.internal.account;

import com.kutay.exchange.modules.ledger.infrastructure.persistence.AccountRepository;
import com.kutay.exchange.modules.ledger.internal.account.model.Account;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountScope;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountState;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.AccountType;
import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.internal.account.model.enums.SystemAccountPurpose;
import com.kutay.exchange.shared.contracts.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerAccountFactory {
    private final AccountRepository accountRepository;

    @Transactional
    public void createUserLedger(LedgerAccountSpec ledgerAccountSpec) {
        List<Account> userLedger = accountRepository
                .findByWalletIdAndAsset(ledgerAccountSpec.walletId(), ledgerAccountSpec.asset());

        if (!userLedger.isEmpty()) {
            log.debug("User ledger already exists for walletId={}, asset={}",
                    ledgerAccountSpec.walletId(),
                    ledgerAccountSpec.asset());
            return;
        }

        for (AccountState state : AccountState.values()) {
            accountRepository.save(
                    Account.createUserAccount(
                            ledgerAccountSpec.walletId(),
                            ledgerAccountSpec.asset(),
                            ledgerAccountSpec.metadata(),
                            state)
            );
        }
    }

    @Transactional
    public void createSystemLedger(Asset asset, AccountType accountType, SystemAccountPurpose purpose, AccountState state) {
        String code = AccountCodeGenerator.generateSystem(accountType, asset, purpose, state);

        Optional<Account> account = accountRepository.findByCode(code);
        if (account.isPresent()) {
            return;
        }

        try {
            accountRepository.save(
                    Account.createSystemAccount(asset, accountType, purpose, null, state)
            );
        } catch (DataIntegrityViolationException
                exception) {
            log.debug("System account already created  89by concurrent process: {}", code);
        }
    }
}
