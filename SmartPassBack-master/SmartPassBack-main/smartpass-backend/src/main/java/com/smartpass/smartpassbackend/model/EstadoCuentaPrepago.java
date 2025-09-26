package com.smartpass.smartpassbackend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pro_estado_cuenta_prepago")
public class EstadoCuentaPrepago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstado;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_contrato")
    private Integer idContrato;

    private String periodo;
    private BigDecimal saldoInicial;
    private BigDecimal cargos;
    private BigDecimal abonos;
    private BigDecimal saldoFinal;

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
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    public BigDecimal getCargos() {
        return cargos;
    }
    public void setCargos(BigDecimal cargos) {
        this.cargos = cargos;
    }
    public BigDecimal getAbonos() {
        return abonos;
    }
    public void setAbonos(BigDecimal abonos) {
        this.abonos = abonos;
    }
    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }
    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

}