
package com.kutay.exchange.modules.auth.domain;

import com.kutay.exchange.modules.auth.api.AuthFacade;
import com.kutay.exchange.modules.auth.web.dto.AuthResponse;
import com.kutay.exchange.modules.auth.web.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

    private final AuthServiceImpl authService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        return authService.register(request);
    }

    @Override
    public AuthResponse login(RegisterRequest request) {
        return authService.login(request);
    }

}