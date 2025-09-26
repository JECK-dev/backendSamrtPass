package com.smartpass.smartpassbackend.repository;


import com.smartpass.smartpassbackend.model.Plaza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlazaRepository extends JpaRepository<Plaza, Integer> {
}