package com.example.demo.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;


    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    // ✅ Generate Tokens
    public Map<String, String> generateTokens(String userId, String email, String role) {
        String accessToken = createToken(userId, email, role, 60 * 60 * 1000);  // 60 minutes
        String refreshToken = createToken(userId, email, role, 60 * 60 * 1000); // 60 minutes

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }


    // ✅ Create Token
    // ✅ Create Token
    private String createToken(String userId, String email, String role, long expiry) {
        return Jwts.builder()
                .setSubject(email)
                .claim("user_Id", userId)  // ✅ Add userId claim
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(SignatureAlgorithm.HS256, getSecretKeyBytes())
                .compact();
    }

    private byte[] getSecretKeyBytes() {
        return Base64.getDecoder().decode(secretKey);
    }


    public String extractRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }


    public Claims extractClaims(String token) {
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
        return jwsClaims.getBody();
    }

    // ✅ Extract Email
    public String extractEmail(String token) {
            return extractClaims(token).getSubject();
    }
    public String extractUserId(String token) {
        Claims claims = extractClaims(token);
        String userId = claims.get("user_Id", String.class);
        System.out.println("Extracted User ID: " + userId);  // ✅ Debug log
        return userId;
    }

//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//    }

    // ✅ Extract Role
    public String extractRole(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse null token");
        }

        Claims claims = extractClaims(token);
        System.out.println("Extracted Claims: " + claims);

        String role = claims.get("role", String.class);

        if (role == null) {
            System.out.println("Role is missing in the token!");
            throw new IllegalArgumentException("Cannot parse null string for role");
        }

        return role;
    }



    // ✅ Check if Token is Expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // ✅ Validate Token
    public boolean isTokenValid(String token, String email, String role) {
        try {
            Claims claims = extractClaims(token);
            String tokenEmail = claims.getSubject();
            String tokenRole = claims.get("role", String.class);

            // Token should not be expired and email/role should match
            return tokenEmail.equals(email) && tokenRole.equals(role) && !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid Token: " + e.getMessage());
            return false;
        }
    }
}