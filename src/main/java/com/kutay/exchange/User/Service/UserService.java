package com.kutay.exchange.User.Service;

import com.kutay.exchange.User.DTO.UserRequestDTO;
import com.kutay.exchange.User.DTO.UserResponseDTO;

import java.util.List;

public interface UserService {
    public UserResponseDTO createUser(UserRequestDTO dto);
    public void updateUser(UserRequestDTO dto);
    public List<UserResponseDTO> readUsers();
    public UserResponseDTO readUser(Long userId);



}
