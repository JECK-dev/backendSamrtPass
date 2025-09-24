package com.smartpass.smartpassbackend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pro_recargas")
public class Recarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_contrato", nullable = false)
    private Integer idContrato;

    @Column(name = "medio_pago")
    private Integer medioPago;

    @Column(name = "metodo_pago")
    private BigDecimal metodoPago;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "fecha_recarga")
    private LocalDateTime fechaRecarga; // UNIX timestamp

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "hash_blockchain")
    private String hashBlockchain;

    public Integer getIdContrato() {
        return idContrato;
    }
    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }
    public Integer getIdPago() {
        return idPago;

    }
    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }
    public Integer getMedioPago() {
        return medioPago;
    }
    public void setMedioPago(Integer medioPago) {
        this.medioPago = medioPago;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public LocalDateTime getFechaRecarga() {
        return fechaRecarga;
    }
    public void setFechaRecarga(LocalDateTime fechaRecarga) {
        this.fechaRecarga = fechaRecarga;
    }
    public Integer getEstado() {
        return estado;
    }
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    public String getHashBlockchain() {
        return hashBlockchain;
    }

    public void setHashBlockchain(String hashBlockchain) {
        this.hashBlockchain = hashBlockchain;
    }

    public BigDecimal getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(BigDecimal metodoPago) {
        this.metodoPago = metodoPago;
    }
}