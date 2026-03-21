package com.clinica.hospital_api.controller;

import com.clinica.hospital_api.model.Doctor;
import com.clinica.hospital_api.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
@CrossOrigin(origins = "${app.frontend.url}")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> listarDoctores() {
        return doctorService.obtenerTodos();
    }

    @PostMapping
    public Doctor guardarDoctor(@RequestBody Doctor doctor) {
        return doctorService.guardarDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDoctor(@PathVariable Long id) {
        doctorService.eliminarDoctor(id);
        return ResponseEntity.noContent().build();
    }
}