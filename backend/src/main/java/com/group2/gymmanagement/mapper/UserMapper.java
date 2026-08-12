package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.request.RegisterRequest;
import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

   @Mapping(source = "userName", target = "username")
   User toUser(UserCreateRequest userCreateRequest);

   @Mapping(source = "username", target = "userName")
   UserDTO toUserDTO(User user);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   @Mapping(target = "id", ignore = true)
   @Mapping(source = "userName", target = "username")
   User updateUser(UserUpdateRequest request, @MappingTarget User target);

   List<UserDTO> toListUserDTO(List<User> users);

   @Mapping(source = "phoneNumber", target = "phone")
   User registerUser(RegisterRequest registerRequest);
}
