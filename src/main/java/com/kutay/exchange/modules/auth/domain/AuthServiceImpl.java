package com.kutay.exchange.modules.auth.domain;

import com.kutay.exchange.modules.auth.web.dto.AuthResponse;
import com.kutay.exchange.modules.auth.web.dto.RegisterRequest;
import com.kutay.exchange.modules.auth.domain.model.AuthCredentialsEntity;
import com.kutay.exchange.modules.auth.mapper.AuthMapper;
import com.kutay.exchange.modules.customer.api.CustomerFacade;
import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    //    private final CustomerService customerService;
    private final CustomerFacade customerFacade;


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        AuthCredentialsEntity credentials = authMapper.convertToEntity(request);
        credentials.setPassword(passwordEncoder.encode(request.password()));

        CustomerRequest req = authMapper.convertToCustomerRequest(request);
        CustomerResponse customer = customerFacade.createCustomer(req);

        credentials.setCustomerId(customer.id());
        authRepository.save(credentials);

        return authMapper.convertToDTO(credentials, customer);
    }


    public AuthResponse login(RegisterRequest request) {
        return null;
    }


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
