package com.clinica.hospital_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Extraemos el encabezado "Authorization" de la petición
        final String authHeader = request.getHeader("Authorization");

        // 2. Verificamos si trae el token con el formato correcto ("Bearer xxxxx.yyyyy.zzzzz")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Cortamos la palabra "Bearer "

            // 3. Si el token es válido y aún no hemos iniciado sesión en el contexto de Spring...
            if (jwtUtil.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {

                String email = jwtUtil.extractEmail(token);
                String rol = jwtUtil.extractRol(token);

                // 4. Creamos los "permisos" oficiales para Spring Security
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rol));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                // 5. Le decimos a Spring: "Este usuario es legítimo, déjalo pasar".
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Continúa la cadena de filtros (pase el que pase, el guardia ya hizo su revisión)
        chain.doFilter(request, response);
    }
}