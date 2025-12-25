package com.kutay.exchange.modules.customer.domain;

import com.kutay.exchange.modules.customer.domain.model.Customer;
import com.kutay.exchange.modules.customer.mapper.CustomerMapper;
import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = new CustomerMapper();

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest dto) {
        Customer customer = customerMapper.convertToEntity(dto);

        Customer db_customer = customerRepository.save(customer);

        return customerMapper.convertToDTO(db_customer);
    }


    public CustomerResponse readUser(Long userId) {
        Optional<Customer> result = customerRepository.findById(userId);

        Customer user = null;

        if (result.isPresent()) {
            user = result.get();
        } else {
            // not found exception
            throw new RuntimeException("user not found");
        }

        return customerMapper.convertToDTO(user);
    }


    public List<CustomerResponse> readUsers() {
        List<Customer> users = customerRepository.findAll();
        System.out.println(users);

        return customerMapper.convertToDtoList(users);
    }
}
