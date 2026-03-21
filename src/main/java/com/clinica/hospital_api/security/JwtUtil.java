package com.clinica.hospital_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // La misma llave maestra secreta
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    // 1. CREAR TOKEN (Lo que ya teníamos)
    public String generateToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 2. EXTRAER EL CORREO DEL TOKEN
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 3. EXTRAER EL ROL DEL TOKEN
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // 4. VALIDAR QUE EL TOKEN SEA AUTÉNTICO Y NO ESTÉ VENCIDO
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date()); // Retorna true si aún no expira
        } catch (Exception e) {
            return false; // Si atraparon el error (alguien modificó el token), es inválido
        }
    }

    // Método auxiliar para desencriptar el token usando nuestra llave secreta
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}