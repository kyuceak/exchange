package com.kutay.exchange.modules.ledger.web;

import com.kutay.exchange.modules.ledger.api.LedgerFacade;
import com.kutay.exchange.modules.ledger.web.dto.RecordTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestingController {

    private final LedgerFacade ledgerFacade;

    @PostMapping("/transaction/record")
    public UUID recordTransaction(@RequestBody RecordTransactionRequest request) {
        return ledgerFacade.recordGenericTransaction(request);
    }

}
