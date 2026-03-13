package com.kutay.exchange.modules.payment.infrastracture.persistence;

import com.kutay.exchange.modules.payment.domain.models.CryptoPayment;
import com.kutay.exchange.modules.payment.domain.models.enums.CryptoState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface CryptoPaymentRepository extends JpaRepository<CryptoPayment, UUID> {

    @Query("""
            SELECT f.id
            FROM FiatPayment f
            WHERE f.state = :state
            AND f.nextRetryAt < :now
            """)
    List<UUID> findIdsForRetry(@Param("state") CryptoState state, @Param("now") Instant now);
}
