package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.EstadoCuentaPospago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPospagoDetalle;
import com.smartpass.smartpassbackend.model.EstadoCuentaPrepago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPrepagoDetalle;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPrepagoRepository;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPospagoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EstadoCuentaService {

    private final EstadoCuentaPrepagoRepository prepagoDetalleRepo;
    private final EstadoCuentaPospagoRepository pospagoDetalleRepo;
    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EstadoCuentaService(EstadoCuentaPrepagoRepository prepagoDetalleRepo,
                               EstadoCuentaPospagoRepository pospagoDetalleRepo,
                               JdbcTemplate jdbcTemplate) {
        this.prepagoDetalleRepo = prepagoDetalleRepo;
        this.pospagoDetalleRepo = pospagoDetalleRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================
    // SPs: Generación de estados
    // ============================

    /**
     * Ejecuta manualmente el SP que genera los estados de cuenta prepago.
     */
    public void generarEstadosPrepago(LocalDate fechaRef) {
        entityManager.createNativeQuery("CALL sp_generar_estado_cuenta_prepago(:fecha)")
                .setParameter("fecha", fechaRef)
                .executeUpdate();
    }

    /**
     * Ejecuta manualmente el SP que genera los estados de cuenta pospago.
     */
    public void generarEstadosPospago(LocalDate fechaRef) {
        entityManager.createNativeQuery("CALL sp_generar_estado_cuenta_pospago(:fecha)")
                .setParameter("fecha", fechaRef)
                .executeUpdate();
    }

    /**
     * Programa ejecución automática de los SP el 1er día de cada mes a las 02:00 AM.
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void generarMensual() {
        LocalDate fechaRef = LocalDate.now().withDayOfMonth(1);
        generarEstadosPrepago(fechaRef);
        generarEstadosPospago(fechaRef);
    }

    // ============================
    // PREPAGO
    // ============================

    /**
     * Resumen de estado de cuenta prepago para un contrato y periodo (saldo inicial, cargos, abonos, saldo final).
     */
    @Transactional
    public EstadoCuentaPrepago getPrepagoResumen(Integer idContrato, String periodo) {
        String sql = """
                SELECT 
                    COALESCE(SUM(CASE WHEN d.monto < 0 THEN d.monto END), 0) * -1 as cargos,
                    COALESCE(SUM(CASE WHEN d.monto > 0 THEN d.monto END), 0) as abonos,
                    0 as saldo_inicial,
                    COALESCE(SUM(d.monto), 0) as saldo_final
                FROM pro_estado_cuenta_prepago_detalle d
                JOIN pro_estado_cuenta_prepago e ON d.id_estado = e.id_estado
                WHERE e.id_contrato = ?
                  AND to_char(d.fecha, 'YYYYMM') = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            EstadoCuentaPrepago resumen = new EstadoCuentaPrepago();
            resumen.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
            resumen.setCargos(rs.getBigDecimal("cargos"));
            resumen.setAbonos(rs.getBigDecimal("abonos"));
            resumen.setSaldoFinal(rs.getBigDecimal("saldo_final"));
            return resumen;
        }, idContrato, periodo);
    }

    /**
     * Lista de movimientos (detalle) del estado de cuenta prepago.
     */
    public List<EstadoCuentaPrepagoDetalle> getPrepagoMovimientos(Integer idContrato, String periodo) {
        return prepagoDetalleRepo.findByContratoAndPeriodo(idContrato, periodo);
    }

    // ============================
    // POSPAGO
    // ============================

    /**
     * Resumen de estado de cuenta pospago para un contrato y periodo (facturado, pagado, saldo pendiente).
     */
    @Transactional
    public EstadoCuentaPospago getPospagoResumen(Integer idContrato, String periodo) {
        String sql = """
                SELECT 
                    COALESCE(SUM(d.total), 0) as monto_facturado,
                    COALESCE(SUM(CASE WHEN d.estado = 'PAGADO' THEN d.total END), 0) as monto_pagado,
                    COALESCE(SUM(CASE WHEN d.estado <> 'PAGADO' THEN d.total END), 0) as saldo_pendiente
                FROM pro_estado_cuenta_pospago_detalle d
                JOIN pro_estado_cuenta_pospago e ON d.id_estado = e.id_estado
                WHERE e.id_contrato = ?
                  AND to_char(d.fecha_emision, 'YYYYMM') = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            EstadoCuentaPospago resumen = new EstadoCuentaPospago();
            resumen.setMontoFacturado(rs.getBigDecimal("monto_facturado"));
            resumen.setMontoPagado(rs.getBigDecimal("monto_pagado"));
            resumen.setSaldoPendiente(rs.getBigDecimal("saldo_pendiente"));
            return resumen;
        }, idContrato, periodo);
    }

    /**
     * Lista de facturas y pagos (detalle) del estado de cuenta pospago.
     */
    public List<EstadoCuentaPospagoDetalle> getPospagoMovimientos(Integer idContrato, String periodo) {
        return pospagoDetalleRepo.findByContratoAndPeriodo(idContrato, periodo);
    }
}
