package com.smartpass.smartpassbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pro_tipo_reclamo")
public class TipoReclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_reclamo")
    private Integer idTipoReclamo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // --- Getters & Setters ---
    public Integer getIdTipoReclamo() { return idTipoReclamo; }
    public void setIdTipoReclamo(Integer idTipoReclamo) { this.idTipoReclamo = idTipoReclamo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}