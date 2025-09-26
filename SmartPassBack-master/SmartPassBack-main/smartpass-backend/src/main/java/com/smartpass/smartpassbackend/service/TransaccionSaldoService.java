package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.TransaccionSaldo;
import com.smartpass.smartpassbackend.repository.TransaccionSaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionSaldoService {

    @Autowired
    private TransaccionSaldoRepository transaccionRepo;

    public List<TransaccionSaldo> obtenerTransaccionesPorContrato(Integer idContrato) {
        return transaccionRepo.obtenerPorContrato(idContrato);
    }

    public List<TransaccionSaldo> getMovimientosPorContrato(Integer idContrato, String periodo) {
        return transaccionRepo.findByContratoAndPeriodo(idContrato, periodo);
    }

    public List<TransaccionSaldo> getMovimientosPorClienteContrato(Integer idCliente, Integer idContrato, String periodo) {
        return transaccionRepo.findByClienteContratoPeriodo(idCliente, idContrato, periodo);
    }
}
