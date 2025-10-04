package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.PasswordResetToken;
import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.repository.PasswordResetTokenRepository;
import com.smartpass.smartpassbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public String generarToken(String email) {
        Usuario usuario = usuarioRepository.findByUsuario(email)
                .orElseThrow(() -> new RuntimeException("No existe usuario con ese correo"));

        String token = java.util.UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setIdUsuario(usuario.getIdUsuario());
        resetToken.setExpiryDate(java.time.LocalDateTime.now().plusHours(1));

        tokenRepository.save(resetToken);

        // Link para frontend
        String resetLink = "http://localhost:4200/reset-password?token=" + token;

        // Enviar correo
        emailService.enviarCorreo(
                usuario.getUsuario(), // en tu entidad, "usuario" guarda el correo
                "Recuperación de contraseña - SmartPass",
                "Hola " + usuario.getNombre() + ",\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n" +
                        "Haz clic en el siguiente enlace para continuar:\n" + resetLink + "\n\n" +
                        "Si no solicitaste este cambio, ignora este correo."
        );

        return resetLink;
    }

    public void resetearPassword(String token, String nuevaPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o no encontrado"));

        // Verificamos si el token está expirado
        if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        // Buscamos el usuario al que pertenece el token
        Usuario usuario = usuarioRepository.findById(resetToken.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Encriptamos y actualizamos la contraseña
        usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaPassword));
        usuarioRepository.save(usuario);

        // Eliminamos el token usado
        tokenRepository.delete(resetToken);
    }


}

