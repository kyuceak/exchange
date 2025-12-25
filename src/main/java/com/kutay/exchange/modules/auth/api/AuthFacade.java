package com.kutay.exchange.modules.auth.api;

import com.kutay.exchange.modules.auth.web.dto.AuthResponse;
import com.kutay.exchange.modules.auth.web.dto.RegisterRequest;

public interface AuthFacade {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(RegisterRequest request);
}