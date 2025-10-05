package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query("SELECT c.monto FROM Categoria c WHERE c.idCategoria = :idCategoria")
    BigDecimal findMontoPeajeByIdCategoria(@Param("idCategoria") int idCategoria);

}
