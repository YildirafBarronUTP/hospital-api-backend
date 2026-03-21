package com.clinica.hospital_api.controller;

import com.clinica.hospital_api.model.Medicamento;
import com.clinica.hospital_api.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    public List<Medicamento> listarMedicamentos() {
        return medicamentoService.obtenerTodos();
    }

    @PostMapping
    public Medicamento guardarMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.guardarMedicamento(medicamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable Long id) {
        medicamentoService.eliminarMedicamento(id);
        return ResponseEntity.noContent().build();
    }
}