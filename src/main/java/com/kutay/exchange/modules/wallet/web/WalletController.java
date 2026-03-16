package com.kutay.exchange.modules.wallet.web;

import com.kutay.exchange.modules.wallet.application.queries.WalletQueryService;
import com.kutay.exchange.modules.wallet.web.dto.queries.BalanceInfo;
import com.kutay.exchange.shared.contracts.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/wallet")
public class WalletController {
    private final WalletQueryService walletQueryService;


    @GetMapping("/{walletId}/balance")
    public Optional<BalanceInfo> getBalance(@PathVariable UUID walletId,
                                            @RequestParam Asset asset) {
        return walletQueryService.getBalance(walletId, asset);
    }


}
