package com.smartpass.smartpassbackend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pro_estado_cuenta_pospago")
public class EstadoCuentaPospago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstado;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_contrato")
    private Integer idContrato;

    private String periodo;
    private BigDecimal montoFacturado;
    private BigDecimal montoPagado;
    private BigDecimal saldoPendiente;

    @Column(name = "fecha_generado")
    private LocalDateTime fechaGenerado;


    // Getters y Setters

    public Integer getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Integer idCliente) {

        this.idCliente = idCliente;
    }
    public Integer getIdContrato() {
        return idContrato;
    }
    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }
    public String getPeriodo() {
        return periodo;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    public BigDecimal getMontoFacturado() {
        return montoFacturado;
    }
    public void setMontoFacturado(BigDecimal montoFacturado) {
        this.montoFacturado = montoFacturado;
    }
    public BigDecimal getMontoPagado() {
        return montoPagado;
    }
    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }
    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }
    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }
    public LocalDateTime getFechaGenerado() {
        return fechaGenerado;
    }
    public void setFechaGenerado(LocalDateTime fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }
    public Integer getIdEstado() {
        return idEstado;
    }
    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }



}