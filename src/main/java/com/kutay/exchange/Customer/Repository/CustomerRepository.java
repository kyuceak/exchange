package com.kutay.exchange.Customer.Repository;


import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
//
//    Optional<UserEntity> findByEmail(String email);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);

}
