package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Reclamo;
import com.smartpass.smartpassbackend.repository.ReclamoRepository;
import org.springframework.stereotype.Service;

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
}
