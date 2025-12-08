package com.group2.gymmanagement.config.Security;


import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.findByRole(UserRole.ADMIN).isEmpty()) {
      User admin = User.builder()
          .username("admin")
          .email("admin@gmail.com")
          .fullName("System Admin")
          .password(passwordEncoder.encode("123456"))
          .phone("0123456789")
          .role(UserRole.ADMIN)
          .status("1")
          .build();

      userRepository.save(admin);
      System.out.println("Default admin created");
    }
  }
}

