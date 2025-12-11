package com.kutay.exchange.modules.Customer.Model.Mapper;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.common.Mapper.BaseMapper;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper<CustomerEntity, CustomerRequest, CustomerResponse> {
    @Override
    public CustomerEntity convertToEntity(CustomerRequest dto, Object... args) {
        CustomerEntity customer = new CustomerEntity();

        if (dto != null) {
            customer.setFirstName(dto.firstName());
            customer.setLastName(dto.lastName());
            customer.setPhoneNumber(dto.phoneNumber());
            customer.setAddress(dto.address());
        }

        return customer;
    }

    @Override
    public com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse convertToDTO(CustomerEntity entity, Object... args) {
        com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse userResponse = new com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse(
                entity.getId(),
                entity.getFirstName()
                , entity.getLastName()
                , entity.getAddress()
                , entity.getPhoneNumber());

        return userResponse;
    }
}
