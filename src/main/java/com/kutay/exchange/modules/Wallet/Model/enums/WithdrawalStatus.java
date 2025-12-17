package com.kutay.exchange.modules.Wallet.Model.enums;

public enum WithdrawalStatus {
    REQUESTED, // customer sent request
    VALIDATED, // request validated
    BROADCASTED, // sent to blockchain
    CONFIRMED, // confirmed in blockchain
    FAILED, // encountered an error
    CANCELLED // user cancelled the request
}
