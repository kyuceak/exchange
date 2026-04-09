package com.kutay.exchange.modules.payment.application.service;

import com.kutay.exchange.modules.auth.api.AuthenticationPrincipal;
import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.api.dto.LedgerIntent;
import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.infrastracture.external.BankApiClient;
import com.kutay.exchange.modules.payment.infrastracture.external.WithdrawRejectException;
import com.kutay.exchange.modules.payment.infrastracture.persistence.BankTransferRepository;
import com.kutay.exchange.modules.payment.web.dto.FiatWithdrawRequest;
import com.kutay.exchange.modules.payment.web.dto.PaymentResponse;
import com.kutay.exchange.modules.wallet.api.WalletFacade;
import com.kutay.exchange.shared.contracts.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final LedgerFacade ledgerFacade;
    private final WalletFacade walletFacade;
    private final BankTransferRepository bankTransferRepository;
    private final BankApiClient bankApiClient;

    @Transactional
    public String initiateWithdrawal(FiatWithdrawRequest request, AuthenticationPrincipal userDetails) {
        // 1. create bankTransfer
        // 2. reserve balance in ledger
        // 3. initiate bank deposit from bank api
        // 4. get response from bank on our status webhook.
        UUID walletId = walletFacade.getWalletId(userDetails.customerId(), request.walletType());

        BankTransfer bankTransfer = createWithdraw(request, walletId);
        bankTransferRepository.save(bankTransfer);

        LedgerIntent intent = LedgerIntent.reserve(walletId, request.asset(), request.amount(), bankTransfer.getReferenceId());
        ledgerFacade.reserve(intent);

        try {
            bankTransfer.markProcessing();
            bankApiClient.initiateWithdraw(bankTransfer);

        } catch (WithdrawRejectException e) {
            log.error(e.getMessage());
        }

        return bankTransfer.getId().toString();
    }

    public BankTransfer createWithdraw(FiatWithdrawRequest request, UUID walletId) {
        if (request.swift() == null || request.swift().isBlank()) {
            return BankTransfer.localWithdraw(walletId,
                    request.asset(),
                    request.amount(),
                    null,
                    null,
                    request.iban(),
                    request.accountHolder());
        }

        return BankTransfer.internationalWithdraw(walletId,
                request.asset(),
                request.amount(),
                null,
                null,
                request.iban(),
                request.accountHolder(),
                request.swift());
    }

}
