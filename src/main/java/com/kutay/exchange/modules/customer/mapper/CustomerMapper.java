package com.kutay.exchange.modules.customer.mapper;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.domain.model.Customer;
import com.kutay.exchange.shared.Mapper.BaseMapper;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomerMapper extends BaseMapper<Customer, CustomerRequest, CustomerResponse> {
    @Override
    public Customer convertToEntity(CustomerRequest dto, Object... args) {
        Objects.requireNonNull(dto);
        return new Customer(dto.nationalId(), dto.firstName(), dto.lastName(), dto.phoneNumber(), dto.address());
    }

    @Override
    public CustomerResponse convertToDTO(Customer entity, Object... args) {
        return new CustomerResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAddress(),
                entity.getPhoneNumber());
    }
}
