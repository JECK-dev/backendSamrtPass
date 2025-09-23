package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.TipoReclamo;
import com.smartpass.smartpassbackend.repository.TipoReclamoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-reclamo")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoReclamoController {

    private final TipoReclamoRepository tipoReclamoRepo;

    public TipoReclamoController(TipoReclamoRepository tipoReclamoRepo) {
        this.tipoReclamoRepo = tipoReclamoRepo;
    }

    @GetMapping
    public List<TipoReclamo> listarTiposReclamo() {
        return tipoReclamoRepo.findAll();
    }
}