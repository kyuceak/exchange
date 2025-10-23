package com.kutay.exchange.Customer.Model.Mapper;

import com.kutay.exchange.Auth.DTO.RegisterRequest;
import com.kutay.exchange.common.DTO.UserResponseDTO;
import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.common.Mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper extends BaseMapper<CustomerEntity, RegisterRequest, UserResponseDTO> {
    @Override
    public CustomerEntity convertToEntity(RegisterRequest dto, Object... args) {
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
    public UserResponseDTO convertToDTO(CustomerEntity entity, Object... args) {
        UserResponseDTO userResponse = new UserResponseDTO(
                entity.getFirstName()
                , entity.getLastName()
                , entity.getAddress()
                , entity.getPhoneNumber());

        return userResponse;
    }
}
