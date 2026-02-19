package com.kutay.exchange.modules.customer.web;

import com.kutay.exchange.modules.customer.domain.CustomerServiceImpl;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{userId}")
    public CustomerResponse getUsers(@PathVariable UUID userId) {
        return customerService.readUser(userId);
    }

    @GetMapping
    public List<CustomerResponse> getUsers() {
        return customerService.readUsers();
    }


}
