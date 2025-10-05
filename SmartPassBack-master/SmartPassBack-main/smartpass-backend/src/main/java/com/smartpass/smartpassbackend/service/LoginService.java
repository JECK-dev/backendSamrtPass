package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Login;
import com.smartpass.smartpassbackend.model.Usuario;
import com.smartpass.smartpassbackend.repository.LoginRepository;
import com.smartpass.smartpassbackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 🔹 usa comparación directa por ahora (no passwordEncoder)
    public Usuario login(String usuarioIngresado, String passwordIngresada) {
        Usuario usuario = usuarioRepository.findByDni(usuarioIngresado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Comparar correctamente usando BCrypt

        if (!passwordEncoder.matches(passwordIngresada, usuario.getPassword())) {
            System.out.println("Contraseña no coincide");
            throw new RuntimeException("Credenciales inválidas");
        }
        return usuario;
    }
}