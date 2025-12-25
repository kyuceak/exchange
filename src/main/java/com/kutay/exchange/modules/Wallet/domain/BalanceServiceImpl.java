package com.kutay.exchange.modules.Wallet.domain;

import com.kutay.exchange.modules.Wallet.infrastructure.persistence.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl {

    private final BalanceRepository balanceRepository;


    void apply() {

    }

}
