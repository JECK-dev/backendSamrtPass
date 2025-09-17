package com.smartpass.smartpassbackend.service;


import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        if (usuario.getUsuario() == null || usuario.getUsuario().isBlank()) {
            throw new IllegalArgumentException("El campo 'usuario' es obligatorio.");
        }
        if (repo.existsByUsuario(usuario.getUsuario())) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        // SIN hashing (solo DEV)
        return repo.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setDni(usuario.getDni());
        existente.setNumTelefono(usuario.getNumTelefono());
        existente.setUsuario(usuario.getUsuario());
        existente.setIdCliente(usuario.getIdCliente());
        existente.setIdRol(usuario.getIdRol());
        // No tocamos password aquí
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Usuario no encontrado");
        repo.deleteById(id);
    }

    /** Cambio de contraseña simple (texto plano) */
    public void cambiarPassword(Long idUsuario, String actual, String nueva, String confirmar) {
        if (nueva == null || nueva.isBlank())
            throw new IllegalArgumentException("La nueva contraseña es obligatoria.");
        if (!nueva.equals(confirmar))
            throw new IllegalArgumentException("Las contraseñas no coinciden.");

        Usuario u = repo.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar contraseña actual (texto plano)
        if (u.getPassword() != null && !u.getPassword().equals(actual)) {
            throw new IllegalArgumentException("La contraseña actual no es correcta.");
        }

        u.setPassword(nueva);
        repo.save(u);
    }
}