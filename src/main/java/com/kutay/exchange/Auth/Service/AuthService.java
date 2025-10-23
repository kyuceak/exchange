package com.kutay.exchange.Auth.Service;

import com.kutay.exchange.Auth.DTO.AuthResponse;
import com.kutay.exchange.Auth.DTO.RegisterRequest;


public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(RegisterRequest request);

}
