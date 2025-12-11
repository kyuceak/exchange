package com.kutay.exchange.modules.Customer.api;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;


import java.util.List;

public interface CustomerFacade {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse readUser(Long customerId);

    List<CustomerResponse> readUsers();
}
