package com.uca.parcialfinalncapas.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY; // Clave secreta para el JWT (se define en el archivo application.yml)
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora = 60 segundos * 60 minutos * 1000 milisegundos

    private Key getSigningKey() { 
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) { // Extrae el nombre de usuario del token
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) { 
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username, String role) { // Genera un token JWT
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) { // Verifica si el token es válido
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) { // Verifica si el token ha expirado
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) { // Extrae la fecha de expiración del token
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractRole(String token) { // Extrae el rol del token
        final Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
} 