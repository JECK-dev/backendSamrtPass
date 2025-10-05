package com.smartpass.smartpassbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ActualizarSaldoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Ejecuta el procedimiento almacenado sp_actualizar_saldo_final cada 5 segundos
     */
    @Scheduled(fixedRate = 5000) // 5000 ms = 5 segundos
    public void ejecutarActualizacionDeSaldos() {
        try {
            jdbcTemplate.execute("CALL sp_sync_saldo_contrato_desde_transacciones()");
            System.out.println("SP ejecutado correctamente a las " + java.time.LocalTime.now());
        } catch (Exception e) {
            System.err.println("Error al ejecutar SP: " + e.getMessage());
        }
    }
}
