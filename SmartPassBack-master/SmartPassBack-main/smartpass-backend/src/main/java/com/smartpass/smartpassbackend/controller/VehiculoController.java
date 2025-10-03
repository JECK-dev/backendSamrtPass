package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Vehiculo;
import com.smartpass.smartpassbackend.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<Vehiculo> listarTodos() {
        return vehiculoService.listarTodos();
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Vehiculo> listarVehiculosPorCliente(@PathVariable Long idCliente) {
        return vehiculoService.obtenerVehiculosPorCliente(idCliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return vehiculoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-documento/{documento}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosPorDocumento(documento));
    }

    @PutMapping("/desafiliar/{idVehiculo}")
    public ResponseEntity<String> desafiliarVehiculo(@PathVariable Long idVehiculo) {
        vehiculoService.desafiliarVehiculo(idVehiculo);
        return ResponseEntity.ok("Vehículo desafiliado correctamente");
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarVehiculo(@RequestBody Vehiculo vehiculo) {
        Map<String, Object> response = new HashMap<>();
        try {
            vehiculoService.registrarVehiculo(vehiculo);
            response.put("mensaje", "Vehículo registrado correctamente con TAG asignado");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("PLACA_DUPLICADA")) {
                response.put("error", "Placa Registrada!!");
            } else if (errorMsg != null && errorMsg.contains("SIN_TAGS")) {
                response.put("error", "No hay TAGs disponibles");
            } else {
                response.put("error", "Error al registrar vehículo");
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Integer id, @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculo));
    }

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping("/upload-json")
    public ResponseEntity<Map<String, String>> uploadVehiculos(@RequestBody List<Vehiculo> vehiculos) {
        for (Vehiculo v : vehiculos) {
            String result = vehiculoService.insertarVehiculo(
                    v.getPlaca(),
                    v.getIdContrato(),
                    v.getCategoria(),
                    v.getModelo(),
                    v.getColor(),
                    v.getMarca(),
                    v.getIdCliente()
            );

            if (!"OK".equals(result)) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "error", "message", result)
                );
            }
        }

        return ResponseEntity.ok(
                Map.of("status", "success", "message", "Vehículos insertados correctamente 🚗✅")
        );
    }

}