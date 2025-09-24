package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.Transito;
import com.smartpass.smartpassbackend.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransitoRepository extends JpaRepository<Transito, Long> {

    @Query("SELECT t FROM Transito t WHERE t.cliente.idCliente = :idCliente AND t.fecha BETWEEN :inicio AND :fin AND t.facturado = false AND t.contrato.tipoContrato = 'POS'")
    List<Transito> findByClienteAndFecha(Long idCliente, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT t FROM Transito t WHERE t.cliente.idCliente = :idCliente")
    List<Transito> findByIdCliente(Long idCliente);

    @Query("SELECT t FROM Transito t WHERE t.contrato.tipoContrato = :tipoContrato AND t.facturado = false")
    List<Transito> findByTipoContratoAndFacturadoFalse(@Param("tipoContrato") String tipoContrato);

    @Query("""
        SELECT t.trId FROM Transito t
        JOIN Contrato c ON c.idContrato = t.contrato.idContrato
        WHERE c.tipoContrato = 'PRE'
          AND NOT EXISTS (
              SELECT 1 FROM TransaccionSaldo s 
              WHERE s.idTransito = t.trId
          )
        ORDER BY t.fecha ASC
    """)
    List<Long> findPendientes();


}