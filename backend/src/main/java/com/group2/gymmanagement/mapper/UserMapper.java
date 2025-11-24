package com.group2.gymmanagement.mapper;


import com.group2.gymmanagement.dto.request.RegisterRequest;
import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

   User toUser(UserCreateRequest userCreateRequest);

   UserDTO toUserDTO(User user);

   @Mapping(target = "id", ignore = true)
   User updateUser(UserUpdateRequest request, @MappingTarget User target);

   List<UserDTO> toListUserDTO(List<User> users);

   User registerUser(RegisterRequest registerRequest);
}
