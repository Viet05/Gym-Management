package com.group2.gymmanagement.mapper;


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

   public User toUser(UserCreateRequest userCreateRequest);

   public UserDTO toUserDTO(User user);

   @Mapping(target = "id", ignore = true)
   public User updateUser(UserUpdateRequest request, @MappingTarget User target);

   public List<UserDTO> toUserDTO(List<User> users);
}
