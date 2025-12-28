package com.kutay.exchange.modules.customer.mapper;

import com.kutay.exchange.modules.customer.web.dto.CustomerRequest;
import com.kutay.exchange.modules.customer.domain.model.Customer;
import com.kutay.exchange.shared.Mapper.BaseMapper;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper<Customer, CustomerRequest, CustomerResponse> {
    @Override
    public Customer convertToEntity(CustomerRequest dto, Object... args) {
        Customer customer = new Customer();

        if (dto != null) {
            customer.setFirstName(dto.firstName());
            customer.setLastName(dto.lastName());
            customer.setPhoneNumber(dto.phoneNumber());
            customer.setAddress(dto.address());
        }

        return customer;
    }

    @Override
    public CustomerResponse convertToDTO(Customer entity, Object... args) {
        CustomerResponse userResponse = new CustomerResponse(
                entity.getId(),
                entity.getFirstName()
                , entity.getLastName()
                , entity.getAddress()
                , entity.getPhoneNumber());

        return userResponse;
    }
}
