package com.smartpass.smartpassbackend.controller;


import com.smartpass.smartpassbackend.service.EstadoCuentaService;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estado-cuenta")
@CrossOrigin(origins = "http://localhost:4200") // ajusta dominios permitidos
public class EstadoCuentaController {

    private final EstadoCuentaService service;

    // Inyección por constructor (sin Lombok)
    public EstadoCuentaController(EstadoCuentaService service) {
        this.service = service;
    }

    @GetMapping("/contratos")
    public List<Map<String, Object>> contratos(@RequestParam Integer clienteId) {
        return service.listarContratos(clienteId);
    }

    @GetMapping("/estados-cuenta")
    public Map<String, Object> estadoCuenta(
            @RequestParam Integer contratoId,
            @RequestParam String periodo,                 // "YYYY-MM"
            @RequestParam(required = false) String buscar,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return service.obtenerEstadoCuenta(
                contratoId,
                YearMonth.parse(periodo),
                buscar,
                page,
                size
        );
    }
/*
    @GetMapping(value="/estados-cuenta/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(@PathVariable String id) {
        byte[] pdfBytes = service.generarPdf(id); // genera el PDF
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estado_cuenta_" + id + ".pdf")
                .body(pdfBytes);
    }

    @GetMapping(value="/estados-cuenta/{id}/excel",
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> descargarExcel(@PathVariable String id) {
        byte[] xlsxBytes = service.generarExcel(id); // genera el Excel
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estado_cuenta_" + id + ".xlsx")
                .body(xlsxBytes);
    }
*/
}
