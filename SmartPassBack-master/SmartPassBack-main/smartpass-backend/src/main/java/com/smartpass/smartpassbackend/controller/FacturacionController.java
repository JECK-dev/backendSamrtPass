package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Cliente;
import com.smartpass.smartpassbackend.model.Factura;
import com.smartpass.smartpassbackend.model.FacturaPdfConfig;
import com.smartpass.smartpassbackend.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturacionController {




    @Autowired
    private FacturacionService facturacionService;
    @Autowired
    private FacturaPdfConfig facturaPdfConfig;


    @PostMapping("/prepago/{idTransito}")
    public String facturarPrepago(@PathVariable Long idTransito) {
        return facturacionService.generarFacturaPrepago(idTransito);
    }

    @PostMapping("/pospago")
    public String facturarPospago(@RequestParam("inicio") String inicioStr,
                                  @RequestParam("fin") String finStr,
                                  @RequestParam("cliente") Long idCliente) {
        LocalDateTime inicio = LocalDateTime.parse(inicioStr);
        LocalDateTime fin = LocalDateTime.parse(finStr);
        return facturacionService.generarFacturaPospago(inicio, fin, idCliente);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Factura> obtenerFacturasPorCliente(@PathVariable Long idCliente) {
        return facturacionService.obtenerPorCliente(idCliente);
    }

    @GetMapping("/descargar/{serie}/{correlativo}")
    public ResponseEntity<byte[]> descargarFactura(
            @PathVariable int serie,
            @PathVariable String correlativo) {

        String nombreArchivo = "B"+ serie + "-" + correlativo + ".pdf";
        Path ruta = Paths.get(facturaPdfConfig.getPath(), nombreArchivo);

        if (!Files.exists(ruta)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] archivo = Files.readAllBytes(ruta);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                    .body(archivo);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}