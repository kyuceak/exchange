package com.kutay.exchange.Customer.Service;

import com.kutay.exchange.Auth.DTO.RegisterRequest;
import com.kutay.exchange.common.DTO.UserResponseDTO;
import com.kutay.exchange.Customer.Model.Mapper.CustomerMapper;
import com.kutay.exchange.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.Customer.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper = new CustomerMapper();


    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

//    @Override
//    @Transactional
//    public UserResponseDTO createUser(UserRequestDTO dto) {
//        // check for duplicate email and duplicate username
//        UserEntity user = userMapper.convertToEntity(dto);
//
//        userRepository.save(user);
//        // might do other operations, send welcome email, create wallet etc.
//        return userMapper.convertToDTO(user);
//    }
//
//    @Override
//    public UserResponseDTO updateEmail(Long userId, UpdateEmailRequest req) {
//
//        Optional<UserEntity> result = userRepository.findById(userId);
//
//        UserEntity user = null;
//
//        if(result.isPresent())
//        {
//            user = result.get();
//            user.setEmail(req.email());
//            userRepository.save(user);
//        }else{
//            throw new RuntimeException("not found");
//        }
//
//        return userMapper.convertToDTO(user);
//    }

    @Transactional
    @Override
    public CustomerEntity createCustomer(RegisterRequest dto) {


        return null;
    }


    @Override
    public UserResponseDTO readUser(Long userId) {

        Optional<CustomerEntity> result = customerRepository.findById(userId);

        CustomerEntity user = null;

        if (result.isPresent()) {
            user = result.get();
        } else {
            // not found exception
            throw new RuntimeException("user not found");
        }

        return customerMapper.convertToDTO(user);
    }

    @Override
    public List<UserResponseDTO> readUsers() {

        List<CustomerEntity> users = customerRepository.findAll();
        System.out.println(users);


        return customerMapper.convertToDtoList(users);
    }
}
