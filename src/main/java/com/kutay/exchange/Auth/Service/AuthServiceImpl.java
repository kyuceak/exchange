package com.kutay.exchange.Auth.Service;

import com.kutay.exchange.Auth.DTO.AuthResponse;
import com.kutay.exchange.Auth.DTO.RegisterRequest;
import com.kutay.exchange.Auth.Model.Entity.AuthCredentialsEntity;
import com.kutay.exchange.Auth.Model.Mapper.AuthMapper;
import com.kutay.exchange.Auth.Repository.AuthRepository;
import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.Customer.Service.CustomerService;
import com.kutay.exchange.Customer.Service.CustomerServiceImpl;
import com.kutay.exchange.Customer.Model.Mapper.CustomerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final CustomerServiceImpl userService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @Override
//    @Transactional
    public AuthResponse register(RegisterRequest request) {
        AuthCredentialsEntity credentials = authMapper.convertToEntity(request);
        credentials.setPassword(passwordEncoder.encode(request.password()));


        CustomerEntity customer = customerService.createCustomer(request);

        credentials.setCustomerId(customer.getId());
        authRepository.save(credentials);

        return authMapper.convertToDTO(credentials, customer);
    }

    @Override
    public AuthResponse login(RegisterRequest request) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
