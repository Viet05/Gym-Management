package com.group2.gymmanagement.security;

import com.group2.gymmanagement.entities.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 1. Spring Security ko làm việc với User Entity nên ta tạo CustomUserDetails để
 * chuyển đổi User Entity thành UserDetails.
 * 2. UserDetails là interface của Spring Security.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  /**
   * 1. Lấy role từ User Entity
   * Chuyển đổi thànhGrantedAuthority.
   * 2. Trả về collection chứa GrantedAuthority.
   * 3. Vì Spring Security làm việc vớiGrantedAuthority nên ta chuyển đổi role thànhGrantedAuthority.
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !"INACTIVE".equalsIgnoreCase(user.getStatus());
  }

  public Long getId() {
    return user.getId();
  }
}
