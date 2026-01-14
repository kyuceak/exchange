package com.kutay.exchange.modules.ledger.web;

import com.kutay.exchange.modules.ledger.api.dto.LedgerAccountSpec;
import com.kutay.exchange.modules.ledger.internal.account.LedgerAccountFactory;
import com.kutay.exchange.modules.ledger.web.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class AccountController {

    private final LedgerAccountFactory ledgerAccountFactory;

    // refactor with DTOs
    @PostMapping("/accounts")
    public UUID createAccount(@RequestBody CreateAccountRequest request) {
        LedgerAccountSpec spec = new LedgerAccountSpec(request.walletId(), request.asset(), request.metadata());
        return ledgerAccountFactory.createUserAccount(spec);
    }
}
