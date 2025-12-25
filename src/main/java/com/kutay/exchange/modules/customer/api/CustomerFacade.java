package com.kutay.exchange.modules.customer.api;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;


import java.util.List;

public interface CustomerFacade {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse readUser(Long customerId);

    List<CustomerResponse> readUsers();
}
