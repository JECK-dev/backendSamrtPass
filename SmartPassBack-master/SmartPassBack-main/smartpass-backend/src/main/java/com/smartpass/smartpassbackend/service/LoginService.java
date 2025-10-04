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
        System.out.println("🔍 Buscando usuario con DNI: " + usuarioIngresado);

        Usuario usuario = usuarioRepository.findByDni(usuarioIngresado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("🔎 Usuario DB encontrado -> DNI: " + usuario.getDni());
        System.out.println("🔎 Usuario DB password guardado (hash) -> " + usuario.getPassword());
        System.out.println("🔑 Password ingresado -> " + passwordIngresada);

        // ✅ Comparar correctamente usando BCrypt
        if (!passwordEncoder.matches(passwordIngresada, usuario.getPassword())) {
            System.out.println("⚠️ Contraseña no coincide");
            throw new RuntimeException("Credenciales inválidas");
        }

        System.out.println("✅ Login correcto para usuario ID: " + usuario.getIdUsuario());
        return usuario;
    }
}