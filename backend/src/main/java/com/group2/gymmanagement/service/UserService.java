package com.group2.gymmanagement.service;

import com.group2.gymmanagement.Specification.UserSpecification;
import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.entities.Trainer;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.ErrorCode;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.exception.Exception;
import com.group2.gymmanagement.mapper.UserMapper;
import com.group2.gymmanagement.repository.TrainerRepository;
import com.group2.gymmanagement.repository.UserRepository;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  UserRepository userRepository;
  UserMapper userMapper;
  UserSpecification userSpecification;
  PasswordEncoder passwordEncoder;
  TrainerRepository trainerRepo;

  public UserDTO createUser(UserCreateRequest request) {

    if (userRepository.existsByEmail(request.getEmail()))
      throw new Exception(ErrorCode.USER_EXISTED);

    if (userRepository.existsByUsername(request.getUserName()))
      throw new Exception(ErrorCode.USER_EXISTED);

    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    if (request.getRole() == null)
      user
          .setRole(UserRole.MEMBER);

    user.setStatus("1");

    user = userRepository.save(user);

    if (user.getRole() == UserRole.TRAINER) {
      Trainer profile = Trainer
          .builder()
          .user(user)
          .isCompleted(false)
          .experience(0)
          .build();
      trainerRepo.save(profile);
    }

    return userMapper.toUserDTO(user);
  }

  public UserDTO updateUser(Long id, UserUpdateRequest request) {

    User user = userRepository
        .findById(id)
        .orElseThrow(() -> new Exception(ErrorCode.USER_NOT_EXISTED));

    if (!user.getEmail()
        .equals(request.getEmail()) &&
        userRepository
            .existsByEmail(request
                .getEmail())) {
      throw new Exception(ErrorCode.USER_EXISTED);
    }

    userMapper.updateUser(request, user);

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user
          .setPassword(passwordEncoder
              .encode(request
                  .getPassword()));
    }

    return userMapper.toUserDTO(userRepository.save(user));
  }

  public Page<UserDTO> getUser(Map<String, Object> request) {

    int page = request.containsKey("page")
        ? Integer.parseInt(request.get("page").toString())
        : 1;
    int size = request.containsKey("size")
        ? Integer.parseInt(request.get("size").toString())
        : 0;
    Page<User> users = userSpecification
        .getUsersByFilter(request
            ,page
            ,size);
    return users
        .map(userMapper::toUserDTO);
  }

  public void deleteUser(Long id) {
    if (trainerRepo.existsById(id)) {
      trainerRepo.deleteById(id);
    }
    userRepository.deleteById(id);
  }
}
