package com.kutay.exchange.modules.ledger.internal.transaction;

import com.kutay.exchange.modules.ledger.infrastructure.persistence.TransactionRepository;
import com.kutay.exchange.modules.ledger.internal.transaction.model.enums.TransactionType;
import com.kutay.exchange.modules.ledger.internal.transaction.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionFactory {

    private final TransactionRepository transactionRepository;

    @Transactional // check these @transactional after
    public Transaction createOrFindIfExists(String referenceId, String description, TransactionType transactionType) {
        return transactionRepository.findByReferenceId(referenceId)
                .orElseGet(() -> {
                    // if not found, save new
                    Transaction transaction = new Transaction(referenceId, description, transactionType);
                    return transactionRepository.save(transaction);
                });
    }

    // maybe later add findByReferenceId and existsByReferenceId for this service

//    public Optional<Transaction> findByReferenceId(String referenceId) {
//        return transactionRepository.findByReferenceId(referenceId);
//    }
//
//    public boolean existsByReferenceId(String referenceId) {
//        return transactionRepository.existsByReferenceId(referenceId);
//    }
}
