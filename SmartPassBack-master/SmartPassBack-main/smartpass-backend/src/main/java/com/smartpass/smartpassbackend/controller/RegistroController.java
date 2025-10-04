package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Cliente;
import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/registro")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    //  Registro Individual
    @PostMapping("/individual")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = registroService.registrar(request.getCliente(), request.getUsuario());
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario individual registrado con éxito",
                    "idUsuario", usuario.getIdUsuario(),
                    "idCliente", usuario.getIdCliente(),
                    "rol", usuario.getIdRol()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al registrar usuario individual: " + e.getMessage()));
        }
    }

    // Registro Empresa
    @PostMapping("/empresa")
    public ResponseEntity<?> registrarEmpresa(@RequestBody RegistroRequestEmpresa request) {
        try {
            Usuario usuario = registroService.registrarEmpresa(request.getCliente(), request.getUsuario());
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario empresa registrado con éxito",
                    "idUsuario", usuario.getIdUsuario(),
                    "idCliente", usuario.getIdCliente(),
                    "rol", usuario.getIdRol()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al registrar empresa: " + e.getMessage()));
        }
    }

    // ===== Clases auxiliares =====
    public static class RegistroRequest {
        private Cliente cliente;
        private Usuario usuario;

        public Cliente getCliente() { return cliente; }
        public void setCliente(Cliente cliente) { this.cliente = cliente; }
        public Usuario getUsuario() { return usuario; }
        public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    }

    public static class RegistroRequestEmpresa {
        private Cliente cliente;
        private Usuario usuario;

        public Cliente getCliente() { return cliente; }
        public void setCliente(Cliente cliente) { this.cliente = cliente; }
        public Usuario getUsuario() { return usuario; }
        public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    }

    // Obtener perfil
    @GetMapping("/{idCliente}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long idCliente) {
        Optional<Cliente> cliente = registroService.obtenerPerfil(idCliente);
        return cliente.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Cliente no encontrado")));
    }

    // Actualizar perfil
    @PutMapping("/{idCliente}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long idCliente, @RequestBody Cliente nuevosDatos) {
        Optional<Cliente> clienteActualizado = registroService.actualizarPerfil(idCliente, nuevosDatos);
        return clienteActualizado.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "No se pudo actualizar, cliente no encontrado")));
    }
}
