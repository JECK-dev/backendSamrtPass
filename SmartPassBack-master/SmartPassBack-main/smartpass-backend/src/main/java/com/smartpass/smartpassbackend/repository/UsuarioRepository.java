package com.smartpass.smartpassbackend.repository;

import com.smartpass.smartpassbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por usuario (correo o nombre de usuario)
    Optional<Usuario> findByUsuario(String usuario);   // correo o username
    boolean existsByUsuario(String usuario);

    boolean existsByDni(String dni);

    // En UsuarioRepository
    Optional<Usuario> findByDni(String dni);



}