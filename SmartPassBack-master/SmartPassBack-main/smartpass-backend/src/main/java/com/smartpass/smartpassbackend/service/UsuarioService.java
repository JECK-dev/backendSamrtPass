package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.repository.ClienteRepository;
import com.smartpass.smartpassbackend.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final ClienteRepository clienteRepo;

    // ✅ Inyectamos ambos repositorios
    public UsuarioService(UsuarioRepository repo, ClienteRepository clienteRepo) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario crear(Usuario usuario) {
        if (usuario.getUsuario() == null || usuario.getUsuario().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'usuario' es obligatorio.");
        }

        String correo = usuario.getUsuario().trim(); // ✅ Solo quita espacios, mantiene mayúsculas/minúsculas
        usuario.setUsuario(correo);

        // Validar correo exacto en pro_usuario y pro_cliente
        if (repo.existsByUsuario(correo) || clienteRepo.existsByCorreo(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un registro con ese correo.");
        }

        // Validar documento exacto en pro_usuario y pro_cliente
        String dni = usuario.getDni().trim();
        if (repo.existsByDni(dni) || clienteRepo.existsByNumDocumento(dni)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un registro con ese número de documento.");
        }

        return repo.save(usuario);
    }



    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setDni(usuario.getDni());
        existente.setNumTelefono(usuario.getNumTelefono());
        existente.setUsuario(usuario.getUsuario());
        existente.setIdCliente(usuario.getIdCliente());
        existente.setIdRol(usuario.getIdRol());
        // ⚠️ No tocamos password aquí
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        repo.deleteById(id);
    }

    /** Cambio de contraseña simple (texto plano - solo DEV) */
    public void cambiarPassword(Long idUsuario, String actual, String nueva, String confirmar) {
        if (nueva == null || nueva.isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña es obligatoria.");
        if (!nueva.equals(confirmar))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden.");

        Usuario u = repo.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Validar contraseña actual (texto plano)
        if (u.getPassword() != null && !u.getPassword().equals(actual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta.");
        }

        u.setPassword(nueva);
        repo.save(u);
    }
}
