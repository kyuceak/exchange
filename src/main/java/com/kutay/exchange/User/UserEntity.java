package com.kutay.exchange.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // so that spring understands that its a table in the database
@Table(name="users") // to override default names or add constraints
public class UserEntity {


    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_generator"
    )
    @SequenceGenerator(
            name="user_id_generator",
            sequenceName = "user_id_seq",
            schema = "users"
    )
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // db doesn't know how to store ENUM, specify it
    private UserStatus status = UserStatus.ACTIVE; // check later for KYC

    // by default, it creates a new table which contains userid-walletid mapoing
    // by default its Lazy loading, which means child is not fetched when getting parent.
    // if we don't want to create a new table we have to use @JoinColumn
    // in OneToMany scenarios @JoinColumn is preferred. its the faster way of accessing data.
    // new table creation is used for ManyToMany scenarios.

//    @OneToMany(cascade = CascadeType.ALL)
//    private List<WalletEntity> wallets = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime lastLoginAt;
}
