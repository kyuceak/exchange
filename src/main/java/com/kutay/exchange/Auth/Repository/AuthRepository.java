package com.kutay.exchange.Auth.Repository;


import com.kutay.exchange.Auth.Model.Entity.AuthCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthCredentialsEntity, Long> {

    Optional<UserDetails> findByEmail(String email);
}
