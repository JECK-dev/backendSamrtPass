package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Contrato;
import com.smartpass.smartpassbackend.repository.ContratoInfo;
import com.smartpass. smartpassbackend.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = "http://localhost:4200")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @GetMapping
    public List<Contrato> listar() {
        return contratoService.listarContratos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> obtenerPorId(@PathVariable Integer id) {
        return contratoService.obtenerContratoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Contrato> crear(@RequestBody Contrato contrato) {
        Contrato nuevo = contratoService.crearContrato(contrato);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> actualizar(@PathVariable Integer id, @RequestBody Contrato contrato) {
        if (!contratoService.obtenerContratoPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        contrato.setIdContrato(id);
        return ResponseEntity.ok(contratoService.guardarContrato(contrato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (!contratoService.obtenerContratoPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        contratoService.eliminarContrato(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Contrato> getContratosPorCliente(@PathVariable Integer idCliente) {
        return contratoService.obtenerContratosPorCliente(idCliente);
    }

    @GetMapping("/prepago/cliente/{idCliente}")
    public ResponseEntity<List<ContratoInfo>> listarContratosPrepagoPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(contratoService.obtenerContratosPrepagoPorCliente(idCliente));
    }

    @GetMapping("/tipo/{tipo}")
    public List<Contrato> getContratosPorTipo(@PathVariable String tipo) {
        return contratoService.getContratosPorTipo(tipo);
    }

    @GetMapping("/tipo/{tipo}/cliente/{idCliente}")
    public List<Contrato> getContratosPorTipoYCliente(
            @PathVariable String tipo,
            @PathVariable Integer idCliente) {
        return contratoService.getContratosPorTipoYCliente(tipo, idCliente);
    }

    @PutMapping("/{id}/baja")
    public ResponseEntity<Contrato> darBaja(@PathVariable Integer id) {
        Contrato contrato = contratoService.darDeBaja(id);
        return ResponseEntity.ok(contrato);

    }
}