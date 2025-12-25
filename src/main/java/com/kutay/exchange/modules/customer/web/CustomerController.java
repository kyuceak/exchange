package com.kutay.exchange.modules.customer.web;

import com.kutay.exchange.modules.customer.domain.CustomerServiceImpl;
import com.kutay.exchange.modules.customer.web.dto.CustomerResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;


    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }
//
//    @PostMapping("register")
//    public UserResponseDTO createUser(@RequestBody UserRequestDTO request){
//        return userService.createUser(request);
//    }

//    @PatchMapping("/{userId}/email")
//    public UserResponseDTO updateEmail(@PathVariable Long userId, @RequestBody UpdateEmailRequest request){
//        return customerService.updateUser(userId,request);
//    }


    @GetMapping("/{userId}")
    public CustomerResponse getUsers(@PathVariable Long userId) {
        return customerService.readUser(userId);
    }

    @GetMapping
    public List<CustomerResponse> getUsers() {
        return customerService.readUsers();
    }


}
