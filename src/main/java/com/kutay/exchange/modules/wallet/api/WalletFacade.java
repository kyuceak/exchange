package com.kutay.exchange.modules.wallet.api;


import java.util.UUID;

public interface WalletFacade {

    UUID createWallet(WalletAccountSpec spec);
    

}
