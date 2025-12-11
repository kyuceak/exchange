package com.kutay.exchange.modules.Auth.Controller;


import com.kutay.exchange.modules.Auth.Service.AuthFacade;
import com.kutay.exchange.modules.Auth.DTO.AuthResponse;
import com.kutay.exchange.modules.Auth.DTO.RegisterRequest;
import com.kutay.exchange.modules.Auth.Service.AuthServiceImpl;
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