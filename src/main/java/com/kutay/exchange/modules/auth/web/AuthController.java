package com.kutay.exchange.modules.auth.web;


import com.kutay.exchange.modules.auth.api.AuthFacade;
import com.kutay.exchange.modules.auth.web.dto.AuthResponse;
import com.kutay.exchange.modules.auth.web.dto.RegisterRequest;
import com.kutay.exchange.modules.auth.domain.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final AuthFacade authFacade;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authFacade.register(request);
    }
}