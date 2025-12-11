package com.kutay.exchange.modules.Customer.Repository;


import com.kutay.exchange.modules.Customer.Model.Entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
//
//
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);


}
