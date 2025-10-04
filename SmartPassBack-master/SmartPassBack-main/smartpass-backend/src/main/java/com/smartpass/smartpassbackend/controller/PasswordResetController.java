package com.smartpass.smartpassbackend.controller;

import com.smartpass.smartpassbackend.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService resetService;

    // Paso 1: solicitar token
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String token = resetService.generarToken(email);

        // Aquí deberías enviar correo, de momento devolvemos el link mock
        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        return ResponseEntity.ok(Map.of("message", "Correo enviado", "resetLink", resetLink));
    }

    // Paso 2: resetear contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");

        resetService.resetearPassword(token, newPassword);

        return ResponseEntity.ok(Map.of("message", "Contraseña cambiada correctamente"));
    }


}

