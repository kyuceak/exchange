package com.kutay.exchange.Auth.Model.Mapper;

import com.kutay.exchange.Auth.DTO.AuthResponse;
import com.kutay.exchange.Auth.DTO.RegisterRequest;
import com.kutay.exchange.Auth.Model.Entity.AuthCredentialsEntity;
import com.kutay.exchange.Customer.DTO.CustomerRequest;
import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.common.Mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper extends BaseMapper<AuthCredentialsEntity, RegisterRequest, AuthResponse> {
    @Override
    public AuthCredentialsEntity convertToEntity(RegisterRequest dto, Object... args) {
        AuthCredentialsEntity entity = new AuthCredentialsEntity();

        if (dto != null) {
            entity.setEmail(dto.email());
            entity.setRole(dto.role());
        }

        return entity;
    }

    @Override
    public AuthResponse convertToDTO(AuthCredentialsEntity auth, Object... args) {
        if (!(args[0] instanceof CustomerEntity user)) {
            throw new IllegalArgumentException("UserEntity argument missing");
        }


        return new AuthResponse(
                user.getId(),
                auth.getEmail(),
                auth.isEmailVerified(),
                user.getFirstName(),
                user.getLastName(),
                auth.getRole(),
                user.getPhoneNumber(),
                user.getAddress()
        );
    }

    public CustomerRequest convertToCustomerRequest(RegisterRequest dto) {

        CustomerRequest req = new CustomerRequest(dto.firstName(), dto.lastName(), dto.phoneNumber(), dto.address());

        return req;
    }
}
