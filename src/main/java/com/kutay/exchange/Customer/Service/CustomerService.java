package com.kutay.exchange.Customer.Service;

import com.kutay.exchange.Auth.DTO.RegisterRequest;
import com.kutay.exchange.Customer.DTO.CustomerRequest;
import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.common.DTO.UserResponseDTO;

import java.util.List;

public interface CustomerService {
    List<UserResponseDTO> readUsers();

    UserResponseDTO readUser(Long userId);

    CustomerEntity createCustomer(CustomerRequest dto);
}
