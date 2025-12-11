package com.kutay.exchange.modules.Customer.facade;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.Service.CustomerService;
import com.kutay.exchange.modules.Customer.api.CustomerFacade;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {

    private final CustomerService customerService;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @Override
    public CustomerResponse readUser(Long customerId) {
        return customerService.readUser(customerId);
    }

    @Override
    public List<CustomerResponse> readUsers() {
        return customerService.readUsers();
    }
}