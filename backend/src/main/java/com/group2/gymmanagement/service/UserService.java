package com.group2.gymmanagement.service;

import com.group2.gymmanagement.Specification.UserSpecification;
import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.entities.TrainerProfile;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.mapper.UserMapper;
import com.group2.gymmanagement.repository.TrainerProfileRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
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
  TrainerProfileRepository trainerProfileRepository;

  public UserDTO createUser(UserCreateRequest request) {

    if (userRepository.existsByEmail(request.getEmail()))
      throw new RuntimeException("Email already exists");

    if (userRepository.existsByUsername(request.getUserName()))
      throw new RuntimeException("Username already exists");

    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    if (request.getRole() == null)
      user.setRole(UserRole.MEMBER);

    user.setStatus("1");

    user = userRepository.save(user);


    if (user.getRole() == UserRole.TRAINER) {
      TrainerProfile profile = TrainerProfile.builder()
          .user(user)
          .completed(false)
          .experienceYears(0)
          .build();
      trainerProfileRepository.save(profile);
    }

    return userMapper.toUserDTO(user);
  }

  public UserDTO updateUser(Long id, UserUpdateRequest request) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));


    if (!user.getEmail().equals(request.getEmail()) &&
        userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists");
    }


    userMapper.updateUser(request, user);


    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    return userMapper.toUserDTO(userRepository.save(user));
  }

  public List<UserDTO> getUser(Map<String, Object> request) {
    List<User> users = userSpecification.getUsersByFilter(request);
    return userMapper.toListUserDTO(users);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}

