package com.smartpass.smartpassbackend.model;


import jakarta.persistence.*;

@Entity
@Table(name = "pro_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nombre;
    private String apellido;
    private String dni;

    @Column(name = "num_telefono")
    private Integer numTelefono;

    private String usuario; // nombre de usuario o correo
    private String password;

    @Column(name = "id_cliente")
    private Long idCliente; // FK al cliente

    @Column(name = "id_rol")
    private Integer idRol; // FK a pro_rol

    // Getters y setters

    public Long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    public Integer getIdRol() {
        return idRol;
    }
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public Integer getNumTelefono() {
        return numTelefono;
    }
    public void setNumTelefono(Integer numTelefono) {
        this.numTelefono = numTelefono;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
