package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Reclamo;
import com.smartpass.smartpassbackend.repository.ReclamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReclamoService {
    private final ReclamoRepository reclamoRepo;

    public ReclamoService(ReclamoRepository reclamoRepo) {
        this.reclamoRepo = reclamoRepo;
    }

    public Reclamo crearReclamo(Reclamo reclamo) {
        return reclamoRepo.save(reclamo);
    }

    public List<Reclamo> listarPorCliente(Integer idCliente) {
        return reclamoRepo.findByClienteIdCliente(idCliente);
    }

    public List<Reclamo> listarTodos() {
        return reclamoRepo.findAll();
    }

    public Reclamo actualizar(Integer id, Reclamo reclamoActualizado) {
        return reclamoRepo.findById(id).map(r -> {
            r.setEstado(reclamoActualizado.getEstado());
            r.setRespuesta(reclamoActualizado.getRespuesta());
            r.setFechaResolucion(LocalDateTime.now());
            return reclamoRepo.save(r);
        }).orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));
    }

}
