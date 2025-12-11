package com.kutay.exchange.modules.Auth.Service;

import com.kutay.exchange.modules.Auth.DTO.AuthResponse;
import com.kutay.exchange.modules.Auth.DTO.RegisterRequest;

public interface AuthFacade {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(RegisterRequest request);
}