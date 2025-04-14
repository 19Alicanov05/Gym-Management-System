package org.example.gymmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;

public interface LoginService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    List<String> extractRoles(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
