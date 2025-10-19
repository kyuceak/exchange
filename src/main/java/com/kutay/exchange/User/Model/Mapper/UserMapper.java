package com.kutay.exchange.User.Model.Mapper;
import com.kutay.exchange.User.DTO.UserRequestDTO;
import com.kutay.exchange.User.DTO.UserResponseDTO;
import com.kutay.exchange.User.Model.UserEntity;
import org.springframework.beans.BeanUtils;
import java.time.LocalDateTime;

public class UserMapper extends BaseMapper<UserEntity, UserRequestDTO, UserResponseDTO> {
    @Override
    public UserEntity convertToEntity(UserRequestDTO dto, Object... args) {
        UserEntity user = new UserEntity();

        if(dto != null){
            BeanUtils.copyProperties(dto,user);
        }

        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    @Override
    public UserResponseDTO convertToDTO(UserEntity entity, Object... args) {
        return null;
    }
}
