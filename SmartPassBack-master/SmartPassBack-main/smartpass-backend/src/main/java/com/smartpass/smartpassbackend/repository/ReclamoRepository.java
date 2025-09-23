package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {
    List<Reclamo> findByClienteIdCliente(Integer idCliente);
}