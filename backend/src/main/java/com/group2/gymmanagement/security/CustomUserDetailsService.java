package com.group2.gymmanagement.security;

import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
/**
 * CustomUserDetailsService là một implementation của UserDetailsService.
 * Nó chịu trách nhiệm tìm kiếm user trong database và trả về UserDetails.
 */
public class CustomUserDetailsService implements UserDetailsService {

  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    
        //Convert entity User thành UserDetails để Spring Security hiểu được.
    return new CustomUserDetails(user);
  }
}
