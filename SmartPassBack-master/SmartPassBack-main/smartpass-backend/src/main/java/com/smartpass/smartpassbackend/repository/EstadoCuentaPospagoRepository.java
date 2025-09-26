package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.EstadoCuentaPospago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoCuentaPospagoRepository extends JpaRepository<EstadoCuentaPospago, Integer> {
    List<EstadoCuentaPospago> findByIdClienteAndPeriodo(Integer idCliente, String periodo);
    List<EstadoCuentaPospago> findByIdContratoAndPeriodo(Integer idContrato, String periodo);
}