package com.kutay.exchange.modules.ledger.web;

import com.kutay.exchange.modules.ledger.internal.LedgerService;
import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*
 * @RestController --> combines @Controller and @ResponseBody
 * @ResponseBody --> enables automatic seriliazation of the return object to HttpResponse
 * @RestController --> Thus, every request handling methed of the controller class--
 * automatically serializes return objects to HttpResponse.
 * @RequestBody annotation maps the HttpRequest body to a transfer or domain object,--
 * enabling automatic deserialization of the inbound HttpRequest body onto a Java object.
 * @RequestMapping --> to map web requests to spring controller methods. base prefix.
 *
 *
 * */
@RestController
@RequestMapping("/test/ledger")
@RequiredArgsConstructor
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping("/transactions")
    public UUID recordEntry(@RequestBody RecordTransactionRequest request) {
        return ledgerService.recordGenericTransaction(request);
    }
}
