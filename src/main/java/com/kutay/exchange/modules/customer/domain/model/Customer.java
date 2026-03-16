package com.kutay.exchange.modules.customer.domain.model;

import com.kutay.exchange.shared.model.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "customers",
        uniqueConstraints = {@UniqueConstraint(name = "uk_national_id", columnNames = {"national_id"})},
        indexes = {@Index(name = "idx_national_id", columnList = "national_id")}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Customer(String nationalId, String firstName, String lastName, String phoneNumber, String address) {
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.phoneNumber = phoneNumber;
    }

    @Column(nullable = false, updatable = false)
    private String nationalId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;
}
