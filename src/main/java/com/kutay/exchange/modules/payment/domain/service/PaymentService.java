package com.kutay.exchange.modules.payment.domain.service;

import com.kutay.exchange.modules.customer.api.CustomerFacade;
import com.kutay.exchange.modules.customer.domain.model.Customer;
import com.kutay.exchange.modules.payment.application.service.PaymentExecutor;
import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.infrastracture.messaging.PaymentEventPublisher;
import com.kutay.exchange.modules.payment.infrastracture.persistence.BankTransferRepository;
import com.kutay.exchange.modules.payment.web.dto.FiatDepositWebhook;
import com.kutay.exchange.modules.payment.web.dto.FiatWithdrawRequest;
import com.kutay.exchange.modules.wallet.api.WalletFacade;
import com.kutay.exchange.modules.wallet.api.WalletType;
import com.kutay.exchange.shared.contracts.Asset;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final BankTransferRepository bankTransferRepository;
    private final PaymentExecutor paymentExecutor;
    private final PaymentEventPublisher paymentEventPublisher;
    private final WalletFacade walletFacade;
    private final CustomerFacade customerFacade;

    @Transactional
    public void initiateDeposit(FiatDepositWebhook request) {
        Optional<BankTransfer> paymentResult = bankTransferRepository.findByBankRef(request.bankRef());

        if (paymentResult.isPresent()) {
            log.warn("Duplicate webhook received for bankref: {} Skipping processing.", request.bankRef());
            return;
        }
        // check walletId with nationalId and verify request is valid.
        UUID customerId = customerFacade.getCustomerIdByNationalId(request.nationalId());
        UUID walletId = walletFacade.getWalletId(customerId, WalletType.SPOT);

        // 1. save payment in the db (STATE --> CREATED)
        BankTransfer newTransfer = null;

        if (request.swift() == null || request.swift().isEmpty()) {
            newTransfer = BankTransfer.localDeposit(walletId,
                    Asset.valueOf(request.asset()), request.amount(),
                    request.senderIban(), request.bankRef(),
                    request.receivingIban(), request.senderName());
        } else {
            newTransfer = BankTransfer.internationalDeposit(
                    walletId,
                    Asset.valueOf(request.asset()), request.amount(),
                    request.senderIban(), request.bankRef(),
                    request.receivingIban(), request.senderName(),
                    request.swift());
        }

        // 2. emit PaymentReceived event to ledger
        try {
            newTransfer.sendToProvider();
            newTransfer.authorize();
            BankTransfer savedTransfer = bankTransferRepository.save(newTransfer);

            paymentEventPublisher.publish(request, savedTransfer);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException cve && cve.getMessage().contains(BankTransfer.UK_BANK_REF)) {
                throw new DuplicatePaymentException(request.bankRef(), e);
            }
        }

    }

    public BankTransfer iniateWithdrawal(FiatWithdrawRequest request, UserDetails user) {

        return null;
    }

}
