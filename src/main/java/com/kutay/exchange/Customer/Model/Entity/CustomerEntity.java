package com.kutay.exchange.Customer.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity // so that spring understands that its a table in the database
@Table(name = "customers") // to override default names or add constraints
@Getter
@Setter
public class CustomerEntity {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column()
    private String address;

    // by default, it creates a new table which contains userid-walletid mapoing
    // by default its Lazy loading, which means child is not fetched when getting parent.
    // if we don't want to create a new table we have to use @JoinColumn
    // in OneToMany scenarios @JoinColumn is preferred. its the faster way of accessing data.
    // new table creation is used for ManyToMany scenarios.

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<WalletEntity> wallets = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column()
    private LocalDateTime updatedAt;


}
