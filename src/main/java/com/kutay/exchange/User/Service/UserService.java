package com.kutay.exchange.User.Service;

import com.kutay.exchange.User.DTO.UserRequestDTO;
import com.kutay.exchange.User.DTO.UserResponseDTO;

import java.util.List;

public interface UserService {
     UserResponseDTO createUser(UserRequestDTO dto);
     void updateUser(UserRequestDTO dto);
     List<UserResponseDTO> readUsers();
     UserResponseDTO readUser(Long userId);



}
