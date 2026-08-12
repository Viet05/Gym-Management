package com.group2.gymmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService customUserDetailsService;

  public JWTAuthenticationFilter(
      JwtUtils jwtUtils,
      CustomUserDetailsService customUserDetailsService
  ) {
    this.jwtUtils = jwtUtils;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      //Lấy token từ header Authorization
      String token = getJwtFromRequest(request);

      if (token != null && jwtUtils.validateToken(token)) {

        //Lấy username từ token
        String username = jwtUtils.getUsernameFromToken(token);

        //security chưa được set cho request
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

          // Load user từ DB
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

          //Tạo authentication object
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
              );

          authentication.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request)
          );

          //Set vào SecurityContext để Spring Security biết user nào đang gọi API
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

    } catch (Exception e) {
      // Do not expose token parsing details to clients or stdout. The request will proceed
      // unauthenticated and Spring Security will return the appropriate 401/403 response.
      log.warn("Unable to authenticate request using the supplied JWT: {}", e.getMessage());
    }

    //Cho request đi tiếp
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
