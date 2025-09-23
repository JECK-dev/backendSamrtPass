package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Reclamo;
import com.smartpass.smartpassbackend.model.Vehiculo;
import com.smartpass.smartpassbackend.repository.VehiculoRepository;
import com.smartpass.smartpassbackend.service.ReclamoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reclamos")
@CrossOrigin(origins = "http://localhost:4200")
public class ReclamoController {

    private final ReclamoService reclamoService;
    private final VehiculoRepository vehiculoRepository;

    public ReclamoController(ReclamoService reclamoService, VehiculoRepository vehiculoRepository) {
        this.reclamoService = reclamoService;
        this.vehiculoRepository = vehiculoRepository;
    }

    @PostMapping
    public Reclamo crearReclamo(@RequestBody Reclamo reclamo) {
        return reclamoService.crearReclamo(reclamo);
    }

    @GetMapping("/{idCliente}")
    public List<Reclamo> listarPorCliente(@PathVariable Integer idCliente) {
        return reclamoService.listarPorCliente(idCliente);
    }

    @GetMapping("/vehi/{idCliente}")
    public List<Vehiculo> listarVehiculosPorClienteReclamo(@PathVariable Long idCliente) {
        return vehiculoRepository.findByCliente_IdCliente(idCliente);
    }
}
