package com.kutay.exchange.modules.Auth.Model.Mapper;

import com.kutay.exchange.modules.Auth.DTO.AuthResponse;
import com.kutay.exchange.modules.Auth.DTO.RegisterRequest;
import com.kutay.exchange.modules.Auth.Model.Entity.AuthCredentialsEntity;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.common.Mapper.BaseMapper;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
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
        if (!(args[0] instanceof CustomerResponse user)) {
            throw new IllegalArgumentException("UserEntity argument missing");
        }


        return new AuthResponse(
                user.id(),
                auth.getEmail(),
                auth.isEmailVerified(),
                user.firstName(),
                user.lastName(),
                auth.getRole(),
                user.phoneNumber(),
                user.address()
        );
    }

    public CustomerRequest convertToCustomerRequest(RegisterRequest dto) {

        CustomerRequest req = new CustomerRequest(dto.firstName(), dto.lastName(), dto.phoneNumber(), dto.address());

        return req;
    }
}
