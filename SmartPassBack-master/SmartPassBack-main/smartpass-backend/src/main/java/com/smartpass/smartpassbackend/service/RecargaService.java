package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.BlockchainRecarga;
import com.smartpass.smartpassbackend.model.Contrato;
import com.smartpass.smartpassbackend.model.Recarga;
import com.smartpass.smartpassbackend.model.TransaccionSaldo;
import com.smartpass.smartpassbackend.repository.BlockchainRecargaRepository;
import com.smartpass.smartpassbackend.repository.ContratoRepository;
import com.smartpass.smartpassbackend.repository.RecargaRepository;
import com.smartpass.smartpassbackend.repository.TransaccionSaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class RecargaService {

    @Autowired
    private RecargaRepository recargaRepo;

    @Autowired
    private ContratoRepository contratoRepo;

    @Autowired
    private TransaccionSaldoRepository transaccionRepo;

    @Autowired
    private BlockchainRecargaRepository blockchainRepo;

    public String realizarRecarga(Integer idContrato, BigDecimal monto, Integer medioPago, String descripcion) {
        // Simulación de pago Niubiz (mock)
        boolean pagoExitoso = true;

        if (!pagoExitoso) {
            throw new RuntimeException("Pago fallido con Niubiz");
        }

        // 1. Obtener contrato
        Contrato contrato = contratoRepo.findById(idContrato)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        // 2. Obtener el saldo anterior desde pro_transacciones_saldo (última transacción)
        BigDecimal saldoAnterior = transaccionRepo.findUltimoSaldo(idContrato)
                .orElse(BigDecimal.ZERO);

        BigDecimal nuevoSaldo = saldoAnterior.add(monto);

        // 3. Crear datos de la transacción para blockchain
        String datos = "RECARGA|Contrato:" + idContrato + "|Monto:" + monto + "|Fecha:" + LocalDateTime.now();
        String hashAnterior = blockchainRepo.findTopByOrderByIdBloqueDesc()
                .map(BlockchainRecarga::getHashBloque)
                .orElse("0");

        String hashActual = generarHash(hashAnterior + datos);

        // 4. Insertar en pro_blockchain_recargas
        BlockchainRecarga bloque = new BlockchainRecarga();
        bloque.setHashBloque(hashActual);
        bloque.setHashAnterior(hashAnterior);
        bloque.setDatosTransaccion(datos);
        bloque.setFechaTransaccion(Timestamp.from(Instant.now()));
        blockchainRepo.save(bloque);

        // 5. Insertar en pro_recargas usando el hash generado
        Recarga recarga = new Recarga();
        recarga.setIdContrato(idContrato);
        recarga.setMonto(monto);
        recarga.setMetodoPago(monto); // mismo monto
        recarga.setMedioPago(medioPago);
        recarga.setFechaRecarga(LocalDateTime.now());
        recarga.setEstado(1);
        recarga.setHashBlockchain(hashActual);
        recarga = recargaRepo.save(recarga);

        // 6. Insertar en pro_transacciones_saldo
        TransaccionSaldo tx = new TransaccionSaldo();
        tx.setIdContrato(idContrato);
        tx.setTipoTransaccion("RECARGA");
        tx.setMontoAnterior(saldoAnterior);
        tx.setMonto(monto);
        tx.setSaldoFinal(nuevoSaldo);
        tx.setFecha(Timestamp.from(Instant.now()));
        tx.setIdRecarga(recarga.getIdPago());
        tx.setDescripcion(descripcion);
        transaccionRepo.save(tx);

        // 7. Actualizar saldo en pro_contrato (mantener sincronizado)
        contrato.setSaldo(nuevoSaldo);
        contratoRepo.save(contrato);

        return "Recarga realizada con éxito. Hash: " + hashActual;
    }


    private String generarHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash", e);
        }
    }
}
