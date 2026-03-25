package com.kutay.exchange.modules.payment.infrastracture.persistence;

import com.kutay.exchange.modules.payment.domain.models.BankTransfer;
import com.kutay.exchange.modules.payment.domain.models.enums.FiatState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransfer, UUID> {
    Optional<BankTransfer> findByReferenceId(String referenceId);

    Optional<BankTransfer> findByBankRef(String bankRef);

    @Query("""
            SELECT f.id
            FROM BankTransfer f
            WHERE f.state = :state
            AND f.nextRetryAt < :now
            """)
    List<UUID> findIdsForRetry(@Param("state") FiatState state, @Param("now") Instant now);
}
