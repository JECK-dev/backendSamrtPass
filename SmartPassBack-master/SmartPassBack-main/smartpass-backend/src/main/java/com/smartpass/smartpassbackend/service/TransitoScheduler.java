package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.repository.TransaccionSaldoRepository;
import com.smartpass.smartpassbackend.repository.TransitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransitoScheduler {

    @Autowired
    private TransitoRepository transitoRepository;

    @Autowired
    private TransaccionSaldoRepository transaccionSaldoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 60000) // cada 1 min
    public void procesarTransitosPendientes() {
        List<Long> pendientes = transitoRepository.findPendientes();

        for (Long trId : pendientes) {
            try {
                jdbcTemplate.execute("CALL procesar_transito(" + trId + ")");
                System.out.println("Transito procesado: " + trId);
            } catch (Exception e) {
                System.err.println("Error al procesar transito " + trId + ": " + e.getMessage());
            }
        }
    }
}
