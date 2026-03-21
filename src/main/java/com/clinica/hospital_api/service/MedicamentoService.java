package com.clinica.hospital_api.service;

import com.clinica.hospital_api.model.Medicamento;
import com.clinica.hospital_api.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Medicamento guardarMedicamento(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    public void eliminarMedicamento(Long id) {
        medicamentoRepository.deleteById(id);
    }
}