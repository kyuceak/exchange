package com.kutay.exchange.modules.Customer.Model.Mapper;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.Model.Entity.Customer;
import com.kutay.exchange.common.Mapper.BaseMapper;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
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
    public com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse convertToDTO(Customer entity, Object... args) {
        com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse userResponse = new com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse(
                entity.getId(),
                entity.getFirstName()
                , entity.getLastName()
                , entity.getAddress()
                , entity.getPhoneNumber());

        return userResponse;
    }
}
