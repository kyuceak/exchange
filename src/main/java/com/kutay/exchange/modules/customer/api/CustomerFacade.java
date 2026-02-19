package com.kutay.exchange.modules.customer.api;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;


import java.util.List;
import java.util.UUID;

public interface CustomerFacade {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse readUser(UUID customerId);

    List<CustomerResponse> readUsers();

    boolean existsById(UUID id);
}
