package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Cliente;
import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.repository.ClienteRepository;
import com.smartpass.smartpassbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistroService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrar(Cliente cliente, Usuario usuario) {
        Cliente clienteGuardado = clienteRepository.findByNumDocumento(cliente.getNumDocumento())
                .orElseGet(() -> {
                    cliente.setFechaCreacion(LocalDateTime.now());
                    cliente.setFechaModificacion(LocalDateTime.now());
                    return clienteRepository.save(cliente);
                });

        if (usuarioRepository.existsByUsuario(usuario.getUsuario())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        usuario.setIdCliente(clienteGuardado.getIdCliente());
        usuario.setIdRol(2);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // 🔒 encriptar
        return usuarioRepository.save(usuario); // devolvemos el usuario creado
    }

    public Usuario registrarEmpresa(Cliente cliente, Usuario usuario) {
        if (cliente.getNumDocumento() == null || cliente.getNumDocumento().isEmpty()) {
            throw new RuntimeException("El RUC es obligatorio.");
        }

        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaModificacion(LocalDateTime.now());
        Cliente clienteGuardado = clienteRepository.save(cliente);

        usuario.setIdCliente(clienteGuardado.getIdCliente());
        usuario.setIdRol(2);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // 🔒 encriptar
        return usuarioRepository.save(usuario); // devolvemos el usuario creado
    }


    public Optional<Cliente> obtenerPerfil(Long idCliente) {
        return clienteRepository.findById(idCliente);
    }

    public Optional<Cliente> actualizarPerfil(Long idCliente, Cliente nuevosDatos) {
        return clienteRepository.findById(idCliente).map(cliente -> {
            cliente.setNombre(nuevosDatos.getNombre());
            cliente.setApellido(nuevosDatos.getApellido());
            cliente.setCorreo(nuevosDatos.getCorreo());
            cliente.setTelefono(nuevosDatos.getTelefono());
            cliente.setFechaModificacion(LocalDateTime.now());
            return clienteRepository.save(cliente);
        });
    }



}