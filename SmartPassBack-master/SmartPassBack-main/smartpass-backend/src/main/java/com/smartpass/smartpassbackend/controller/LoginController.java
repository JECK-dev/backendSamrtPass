package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.model.Login;
import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        System.out.println("📥 Body recibido en /login: " + body);

        String usuario = body.get("usuario");
        String password = body.get("password");

        System.out.println("➡️ usuario recibido: " + usuario);
        System.out.println("➡️ password recibido: " + password);

        try {
            Usuario user = loginService.login(usuario, password);

            return ResponseEntity.ok(Map.of(
                    "message", "Login exitoso",
                    "idUsuario", user.getIdUsuario(),
                    "idCliente", user.getIdCliente(),
                    "idRol", user.getIdRol(),
                    "nombre", user.getNombre(),
                    "apellido", user.getApellido()
            ));

        } catch (RuntimeException e) {
            System.out.println("❌ Error durante login: " + e.getMessage());
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

}