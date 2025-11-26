package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  UserService userService;

  @PostMapping(value = "/users")
  public ApiResponse<UserDTO> createUser(@RequestBody UserCreateRequest request) {
    return ApiResponse.<UserDTO>builder()
        .code(200)
        .message("Success")
        .data(userService.createUser(request))
        .build();
  }

  @PutMapping(value = "/users/{id}")
  public ApiResponse<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest request) {
    return ApiResponse.<UserDTO>builder()
        .code(200)
        .message("Success")
        .data(userService.updateUser(id, request))
        .build();
  }

  @GetMapping(value = "/users")
  public ApiResponse<List<UserDTO>> getAllUsers(@RequestParam Map<String, Object> request) {
    return ApiResponse.<List<UserDTO>>builder()
        .code(200)
        .message("Success")
        .data(userService.getUser(request))
        .build();
  }

  @DeleteMapping(value = "/users/{id}")
  public String deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return "Success";
  }
}
