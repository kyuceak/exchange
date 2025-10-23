package com.kutay.exchange.Customer.Controller;

import com.kutay.exchange.common.DTO.UserResponseDTO;
import com.kutay.exchange.Customer.Service.CustomerServiceImpl;
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
    public UserResponseDTO getUsers(@PathVariable Long userId) {
        return customerService.readUser(userId);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return customerService.readUsers();
    }


}
