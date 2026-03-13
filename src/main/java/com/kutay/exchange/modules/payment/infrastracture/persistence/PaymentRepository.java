package com.kutay.exchange.modules.payment.infrastracture.persistence;

import com.kutay.exchange.modules.payment.domain.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
