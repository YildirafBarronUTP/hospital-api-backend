package com.clinica.hospital_api.config;

import com.clinica.hospital_api.model.Usuario;
import com.clinica.hospital_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByEmail("admin@sanjuan.com").isEmpty()) {

            Usuario admin = new Usuario();
            admin.setEmail("admin@sanjuan.com");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRol("ROLE_ADMIN");

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario Administrador Maestro creado con éxito.");
        }
    }
}