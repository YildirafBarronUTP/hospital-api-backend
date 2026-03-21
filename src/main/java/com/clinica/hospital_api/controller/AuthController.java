package com.clinica.hospital_api.controller;

import com.clinica.hospital_api.model.Usuario;
import com.clinica.hospital_api.repository.UsuarioRepository;
import com.clinica.hospital_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Permitimos a Next.js conectarse
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        // 1. Buscamos si el usuario existe en la Base de Datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // 2. Comparamos la contraseña enviada con la contraseña encriptada (BCrypt)
            if (passwordEncoder.matches(password, usuario.getPassword())) {

                // 3. ¡Éxito! Fabricamos el Token
                String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());

                // 4. Se lo enviamos al Frontend
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("rol", usuario.getRol());
                response.put("email", usuario.getEmail());

                return ResponseEntity.ok(response);
            }
        }

        // Si el usuario no existe o la contraseña está mal:
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
}