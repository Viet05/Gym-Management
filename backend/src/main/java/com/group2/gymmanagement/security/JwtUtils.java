package com.group2.gymmanagement.security;

import com.group2.gymmanagement.entities.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  private static final String ISSUER = "GymManagement";

  /**
   * Generate JWT token for user
   */
  public String generateToken(User user) {

    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .subject(user.getUsername())
        .issuer(ISSUER)
        .issueTime(new Date())
        .expirationTime(new Date(Instant.now()
            .plus(3, ChronoUnit.HOURS)
            .toEpochMilli()))
        .claim("role", user.getRole().name())
        .claim("userId", user.getUserId())
        .build();

    JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));

    try {
      jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot generate token", e);
      throw new RuntimeException("Cannot generate token");
    }
  }

  /**
   * Validate token expiration + signature
   */
  public boolean validateToken(String token) {

    try {
      JWSObject jwsObject = JWSObject.parse(token);
      JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

      if (!jwsObject.verify(verifier)) {
        log.warn("Invalid signature");
        return false;
      }

      JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

      if (claimsSet.getExpirationTime().before(new Date())) {
        log.warn("Token expired");
        return false;
      }

      return true;

    } catch (Exception e) {
      log.error("Token validation error", e);
      return false;
    }
  }

  /**
   * Extract username from token
   */
  public String getUsernameFromToken(String token) {
    try {
      JWSObject jwsObject = JWSObject.parse(token);
      JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
      return claimsSet.getSubject();

    } catch (Exception e) {
      log.error("Cannot extract username", e);
      return null;
    }
  }

  /**
   * Extract role from token
   */
  public String getRoleFromToken(String token) {
    try {
      JWSObject jwsObject = JWSObject.parse(token);
      JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
      return claimsSet.getStringClaim("role");

    } catch (Exception e) {
      log.error("Cannot extract role", e);
      return null;
    }
  }
}
