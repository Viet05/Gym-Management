package com.group2.gymmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      // 1. Lấy token từ header Authorization
      String token = getJwtFromRequest(request);

      if (token != null && jwtUtils.validateToken(token)) {

        // 2. Lấy username từ token
        String username = jwtUtils.getUsernameFromToken(token);

        // 3. Đảm bảo security chưa được set cho request này
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

          // Load user từ DB
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

          // 4. Tạo authentication object
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
              );

          authentication.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );

          // 5. Set vào SecurityContext để Spring Security biết user nào đang gọi API
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

    } catch (Exception e) {
      log.error("Cannot set user authentication", e);
    }

    // 6. Cho request đi tiếp
    filterChain.doFilter(request, response);
  }

  /**
   * Lấy token từ header Authorization: Bearer <token>
   */
  private String getJwtFromRequest(HttpServletRequest request) {

    String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }

    return null;
  }
}
