package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.TransaccionSaldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionSaldoRepository extends JpaRepository<TransaccionSaldo, Integer> {

    @Query("SELECT t FROM TransaccionSaldo t WHERE t.idContrato = :idContrato ORDER BY t.fecha DESC")
    List<TransaccionSaldo> obtenerPorContrato(@Param("idContrato") Integer idContrato);

    @Query("SELECT SUM(t.monto) FROM TransaccionSaldo t WHERE t.tipoTransaccion = :tipo AND t.idContrato = :idContrato")
    Optional<BigDecimal> sumByIdContrato(@Param("tipo") String tipo, @Param("idContrato") Integer idContrato);


    @Query("""
    SELECT s.saldoFinal 
    FROM TransaccionSaldo s
    WHERE s.idContrato = :idContrato
    ORDER BY s.idTransaccion DESC
    LIMIT 1
    """)
    Optional<BigDecimal> findUltimoSaldo(@Param("idContrato") Integer idContrato);

}
