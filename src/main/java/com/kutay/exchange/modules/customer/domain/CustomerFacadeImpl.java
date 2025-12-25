package com.kutay.exchange.modules.customer.domain;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.api.CustomerFacade;

import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {

    private final CustomerServiceImpl customerService;

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