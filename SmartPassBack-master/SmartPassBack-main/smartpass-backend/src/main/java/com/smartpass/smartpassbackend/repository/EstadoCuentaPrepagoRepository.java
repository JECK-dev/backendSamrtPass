package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.EstadoCuentaPrepago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPrepagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoCuentaPrepagoRepository extends JpaRepository<EstadoCuentaPrepago, Integer> {



    @Query(value = "SELECT d.* " +
            "FROM pro_estado_cuenta_prepago_detalle d " +
            "JOIN pro_estado_cuenta_prepago e ON d.id_estado = e.id_estado " +
            "WHERE e.id_contrato = :idContrato " +
            "AND to_char(d.fecha, 'YYYYMM') = :periodo",
            nativeQuery = true)
    List<EstadoCuentaPrepagoDetalle> findByContratoAndPeriodo(@Param("idContrato") Integer idContrato,
                                                              @Param("periodo") String periodo);
}
