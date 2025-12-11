package com.kutay.exchange.modules.Customer.Service;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;


import java.util.List;

public interface CustomerService {
    List<CustomerResponse> readUsers();

    CustomerResponse readUser(Long userId);

    CustomerResponse createCustomer(CustomerRequest dto);
}
