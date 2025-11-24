package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.mapper.UserMapper;
import com.group2.gymmanagement.repository.UserRepository;
import com.group2.gymmanagement.repository.UserSpecification;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  UserRepository userRepository;
  private final UserMapper userMapper;
  UserSpecification userSpecification;

  public UserDTO createUser(UserCreateRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    User user = userMapper.toUser(request);

    user = userRepository.save(user);

    return userMapper.toUserDTO(user);
  }

  public UserDTO updateUser(Long id, UserUpdateRequest request) {
    User user = userRepository.findById(id).orElse(null);

    return userMapper
        .toUserDTO(userRepository
        .save(userMapper
            .updateUser(request, user)));
  }

  public List<UserDTO> getUser(Map<String, Objects> request) {

    List<User> users = userSpecification.getUserSpec(request);

    return userMapper.toUserDTO(users);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
