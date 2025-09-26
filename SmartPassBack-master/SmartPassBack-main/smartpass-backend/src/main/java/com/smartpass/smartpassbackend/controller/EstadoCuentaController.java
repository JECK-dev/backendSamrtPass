package com.smartpass.smartpassbackend.controller;


import com.smartpass.smartpassbackend.model.EstadoCuentaPospago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPrepago;
import com.smartpass.smartpassbackend.model.TransaccionSaldo;
import com.smartpass.smartpassbackend.service.EstadoCuentaService;
import com.smartpass.smartpassbackend.service.TransaccionSaldoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estado-cuenta")
@CrossOrigin(origins = "http://localhost:4200") // ajusta dominios permitidos
public class EstadoCuentaController {

    private final EstadoCuentaService service;

    private  TransaccionSaldoService transaccionSaldoService;

    public EstadoCuentaController(EstadoCuentaService service) {
        this.service = service;
    }

    // Ejecutar manualmente los SP (si se quiere desde Postman)
    @PostMapping("/generar/prepago")
    public void generarPrepago(@RequestParam String fecha) {
        service.generarEstadosPrepago(LocalDate.parse(fecha));
    }

    @PostMapping("/generar/pospago")
    public void generarPospago(@RequestParam String fecha) {
        service.generarEstadosPospago(LocalDate.parse(fecha));
    }

    // Consultar estados de cuenta
    @GetMapping("/prepago/{idContrato}")
    public List<EstadoCuentaPrepago> getPrepago(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getEstadoCuentaPrepago(idContrato, periodo);
    }

    @GetMapping("/pospago/{idContrato}")
    public List<EstadoCuentaPospago> getPospago(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getEstadoCuentaPospago(idContrato, periodo);
    }

    @GetMapping("/movimientos/contrato/{idContrato}")
    public List<TransaccionSaldo> getMovimientosPorContrato(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return transaccionSaldoService.getMovimientosPorContrato(idContrato, periodo);
    }

    // Movimientos por cliente + contrato + periodo
    @GetMapping("/movimientos/cliente/{idCliente}/contrato/{idContrato}")
    public List<TransaccionSaldo> getMovimientosPorClienteContrato(
            @PathVariable Integer idCliente,
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return transaccionSaldoService.getMovimientosPorClienteContrato(idCliente, idContrato, periodo);
    }
}
