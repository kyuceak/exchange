package com.kutay.exchange.modules.Auth.Service;

import com.kutay.exchange.modules.Auth.DTO.AuthResponse;
import com.kutay.exchange.modules.Auth.DTO.RegisterRequest;
import com.kutay.exchange.modules.Auth.Model.Entity.AuthCredentialsEntity;
import com.kutay.exchange.modules.Auth.Model.Mapper.AuthMapper;
import com.kutay.exchange.modules.Auth.Repository.AuthRepository;
import com.kutay.exchange.modules.Customer.api.CustomerFacade;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.Service.CustomerService;
import com.kutay.exchange.modules.Customer.Service.CustomerServiceImpl;
import com.kutay.exchange.modules.Customer.Model.Mapper.CustomerMapper;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {


    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    //    private final CustomerService customerService;
    private final CustomerFacade customerFacade;

    @Override
//    @Transactional
    public AuthResponse register(RegisterRequest request) {
        AuthCredentialsEntity credentials = authMapper.convertToEntity(request);
        credentials.setPassword(passwordEncoder.encode(request.password()));

        CustomerRequest req = authMapper.convertToCustomerRequest(request);
        CustomerResponse customer = customerFacade.createCustomer(req);

        credentials.setCustomerId(customer.id());
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
