package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.EstadoCuentaPospago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPospagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoCuentaPospagoRepository extends JpaRepository<EstadoCuentaPospago, Integer> {
    List<EstadoCuentaPospago> findByIdClienteAndPeriodo(Integer idCliente, String periodo);
    List<EstadoCuentaPospago> findByIdContratoAndPeriodo(Integer idContrato, String periodo);

    @Query(value = "SELECT d.* " +
            "FROM pro_estado_cuenta_pospago_detalle d " +
            "JOIN pro_estado_cuenta_pospago e ON d.id_estado = e.id_estado " +
            "WHERE e.id_contrato = :idContrato " +
            "AND to_char(d.fecha_emision, 'YYYYMM') = :periodo",
            nativeQuery = true)
    List<EstadoCuentaPospagoDetalle> findByContratoAndPeriodo(@Param("idContrato") Integer idContrato,
                                                              @Param("periodo") String periodo);
}