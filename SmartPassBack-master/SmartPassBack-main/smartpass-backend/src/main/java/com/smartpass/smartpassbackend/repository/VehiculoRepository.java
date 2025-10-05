package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    List<Vehiculo> findByCliente_IdCliente(Long idCliente);

    List<Vehiculo> findByCliente_NumDocumento(String documento);

    Optional<Vehiculo> findById(Long id);

    @Query("SELECT v FROM Vehiculo v WHERE v.idContrato = :idContrato")
    List<Vehiculo> findByContratoId(@Param("idContrato") Integer idContrato);

}