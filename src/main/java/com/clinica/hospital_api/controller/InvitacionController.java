package com.clinica.hospital_api.controller;

import com.clinica.hospital_api.model.TokenInvitacion;
import com.clinica.hospital_api.model.Usuario;
import com.clinica.hospital_api.repository.TokenInvitacionRepository;
import com.clinica.hospital_api.repository.UsuarioRepository;
import com.clinica.hospital_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@CrossOrigin(origins = "${app.frontend.url}")
public class InvitacionController {

    @Autowired
    private TokenInvitacionRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @PostMapping("/enviar")
    public ResponseEntity<?> enviarInvitacion(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String rol = request.get("rol");

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

        String enlaceRegistro = frontendUrl + "/hospital/registro?token=" + tokenUnico;
        String asunto = "Invitación Segura - Clínica San Juan de Dios";
        String mensaje = "Hola,\n\nHas sido invitado a unirte al sistema de gestión de la Clínica San Juan con el nivel de acceso: " + rol + ".\n\nPara activar tu cuenta y crear tu contraseña segura, por favor haz clic en el siguiente enlace (Este enlace expirará en 24 horas):\n\n" + enlaceRegistro + "\n\nSaludos,\nEl equipo de Administración.";

        emailService.enviarCorreo(email, asunto, mensaje);

        return ResponseEntity.ok("Invitación enviada correctamente");
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");

        Optional<TokenInvitacion> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El enlace de invitación es inválido o no existe.");
        }

        TokenInvitacion invitacion = tokenOpt.get();

        if (invitacion.isUsado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este enlace ya fue utilizado para crear una cuenta.");
        }
        if (invitacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El enlace de invitación ha expirado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(invitacion.getEmailDestino());
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRol(invitacion.getRolAsignado());

        usuarioRepository.save(nuevoUsuario);

        invitacion.setUsado(true);
        tokenRepository.save(invitacion);

        return ResponseEntity.ok("Cuenta creada con éxito");
    }
}