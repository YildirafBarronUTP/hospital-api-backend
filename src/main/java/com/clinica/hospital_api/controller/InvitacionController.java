package com.clinica.hospital_api.controller;

import com.clinica.hospital_api.model.TokenInvitacion;
import com.clinica.hospital_api.model.Usuario;
import com.clinica.hospital_api.repository.TokenInvitacionRepository;
import com.clinica.hospital_api.repository.UsuarioRepository;
import com.clinica.hospital_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class InvitacionController {

    @Autowired
    private TokenInvitacionRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // 1. ENVIAR INVITACIÓN (Lo que ya hicimos)
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarInvitacion(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String rol = request.get("rol");

        // Validar si el usuario ya existe para no enviar invitaciones dobles
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya tiene una cuenta activa.");
        }

        String tokenUnico = UUID.randomUUID().toString();

        TokenInvitacion invitacion = new TokenInvitacion();
        invitacion.setToken(tokenUnico);
        invitacion.setEmailDestino(email);
        invitacion.setRolAsignado(rol);
        invitacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        invitacion.setUsado(false);

        tokenRepository.save(invitacion);

        String enlaceRegistro = "http://localhost:3000/hospital/registro?token=" + tokenUnico;
        String asunto = "Invitación Segura - Clínica San Juan de Dios";
        String mensaje = "Hola,\n\nHas sido invitado a unirte al sistema de gestión de la Clínica San Juan con el nivel de acceso: " + rol + ".\n\nPara activar tu cuenta y crear tu contraseña segura, por favor haz clic en el siguiente enlace (Este enlace expirará en 24 horas):\n\n" + enlaceRegistro + "\n\nSaludos,\nEl equipo de Administración.";

        emailService.enviarCorreo(email, asunto, mensaje);

        return ResponseEntity.ok("Invitación enviada correctamente");
    }

    // 2. NUEVO: REGISTRAR AL USUARIO CON SU TOKEN
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");

        // A. Buscamos el token en la base de datos
        Optional<TokenInvitacion> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El enlace de invitación es inválido o no existe.");
        }

        TokenInvitacion invitacion = tokenOpt.get();

        // B. Reglas de Negocio: Validar estado del token
        if (invitacion.isUsado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este enlace ya fue utilizado para crear una cuenta.");
        }
        if (invitacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El enlace de invitación ha expirado.");
        }

        // C. Todo está en orden, creamos el usuario en SQL Server
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(invitacion.getEmailDestino());
        nuevoUsuario.setPassword(passwordEncoder.encode(password)); // ¡Siempre encriptado!
        nuevoUsuario.setRol(invitacion.getRolAsignado());

        usuarioRepository.save(nuevoUsuario);

        // D. "Quemamos" el token para que no se pueda volver a usar
        invitacion.setUsado(true);
        tokenRepository.save(invitacion);

        return ResponseEntity.ok("Cuenta creada con éxito");
    }
}