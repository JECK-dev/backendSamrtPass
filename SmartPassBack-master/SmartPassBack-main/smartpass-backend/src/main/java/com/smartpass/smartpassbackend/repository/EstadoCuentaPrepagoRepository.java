package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.EstadoCuentaPrepago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoCuentaPrepagoRepository extends JpaRepository<EstadoCuentaPrepago, Integer> {
    List<EstadoCuentaPrepago> findByIdClienteAndPeriodo(Integer idCliente, String periodo);
    List<EstadoCuentaPrepago> findByIdContratoAndPeriodo(Integer idContrato, String periodo);
}
