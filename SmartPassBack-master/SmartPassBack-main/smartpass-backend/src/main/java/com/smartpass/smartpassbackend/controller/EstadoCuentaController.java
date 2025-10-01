package com.smartpass.smartpassbackend.controller;


import com.smartpass.smartpassbackend.model.*;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPospagoRepository;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPrepagoRepository;
import com.smartpass.smartpassbackend.service.EstadoCuentaService;
import com.smartpass.smartpassbackend.service.TransaccionSaldoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    // ============================
    // PREPAGO
    // ============================

    // Resumen de cuenta (saldo inicial, cargos, abonos, saldo final)
    @GetMapping("/prepago/resumen/{idContrato}")
    public EstadoCuentaPrepago getPrepagoResumen(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getPrepagoResumen(idContrato, periodo);
    }

    // Movimientos de cuenta (lista de transacciones)
    @GetMapping("/prepago/{idContrato}")
    public List<EstadoCuentaPrepagoDetalle> getPrepagoMovimientos(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getPrepagoMovimientos(idContrato, periodo);
    }

    // ============================
    // POSPAGO
    // ============================

    // Resumen de cuenta (monto facturado, pagado, pendiente)
    @GetMapping("/pospago/resumen/{idContrato}")
    public EstadoCuentaPospago getPospagoResumen(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getPospagoResumen(idContrato, periodo);
    }

    // Detalles de cuenta (facturas/pagos)
    @GetMapping("/pospago/{idContrato}")
    public List<EstadoCuentaPospagoDetalle> getPospagoDetalles(
            @PathVariable Integer idContrato,
            @RequestParam String periodo) {
        return service.getPospagoMovimientos(idContrato, periodo);
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
