package com.kutay.exchange.modules.Customer.Service;

import com.kutay.exchange.modules.Customer.api.DTO.CustomerRequest;
import com.kutay.exchange.modules.Customer.Model.Mapper.CustomerMapper;
import com.kutay.exchange.modules.Customer.Model.Entity.CustomerEntity;
import com.kutay.exchange.modules.Customer.Repository.CustomerRepository;
import com.kutay.exchange.modules.Customer.api.DTO.CustomerResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = new CustomerMapper();

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
    public CustomerResponse createCustomer(CustomerRequest dto) {
        CustomerEntity customer = customerMapper.convertToEntity(dto);

        CustomerEntity db_customer = customerRepository.save(customer);

        return customerMapper.convertToDTO(db_customer);
    }

    @Override
    public CustomerResponse readUser(Long userId) {
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
    public List<CustomerResponse> readUsers() {
        List<CustomerEntity> users = customerRepository.findAll();
        System.out.println(users);

        return customerMapper.convertToDtoList(users);
    }
}
