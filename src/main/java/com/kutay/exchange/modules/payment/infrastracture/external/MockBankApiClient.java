package com.kutay.exchange.modules.payment.infrastracture.external;

import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.domain.models.Payment;
import com.kutay.exchange.modules.payment.web.dto.FiatStatusWebhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class MockBankApiClient implements BankApiClient {
    private final RestClient restClient;

    @Override
    public void initiateWithdraw(BankTransfer withdraw) throws WithdrawRejectException {
        log.warn("MockBank api client returning response for transfer referenceId: {}", withdraw.getBankRef());

        boolean status = Math.random() > 0.1;

        if (!status) {
            throw new WithdrawRejectException("Bank rejected the transfer.");
        }

        sendWebhook(withdraw);
    }

    // push request to our FiatWithdrawal webhook
//    @Async would not work because self invocation in the same class (since proxy is bypassed) so we are using CompletableFuture.runAsync
    public void sendWebhook(BankTransfer withdraw) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000); // delay request

                boolean status = Math.random() > 0.1;

                String bankRef = UUID.randomUUID().toString();
                FiatStatusWebhook payload = new FiatStatusWebhook(withdraw.getReferenceId(), bankRef, status ? "SUCCESS" : "FAILED", Instant.now(), "");
                // add HMAC verification later.
                restClient.post()
                        .uri("http://localhost:8080/api/webhooks/fiat/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "MockBankApiClient/1.0")
                        .body(payload) // set the request body, serialized to JSON
                        .retrieve() //send the request
                        .toBodilessEntity(); // we dont care about response right now. just send it. maybe check later

                log.info("Mock bank webhook sent. bankRef={}", bankRef);
            } catch (Exception e) {
                log.error("Mock bank webhook failed. referenceId: {}", withdraw.getReferenceId(), e);
            }
        });
    }
}
