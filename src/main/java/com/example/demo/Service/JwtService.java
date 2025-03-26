//package com.example.demo.Service;
//
//import io.jsonwebtoken.*;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class JwtService {
//
//    private final String SECRET_KEY = "SuperSecretKey123";
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 4 * 60 * 1000)) // 4 minutes
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException e) {
//            return false;
//        }
//    }
//}
