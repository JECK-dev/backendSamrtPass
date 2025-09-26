package com.smartpass.smartpassbackend.service;


import com.smartpass.smartpassbackend.model.EstadoCuentaPospago;
import com.smartpass.smartpassbackend.model.EstadoCuentaPrepago;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPospagoRepository;
import com.smartpass.smartpassbackend.repository.EstadoCuentaPrepagoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EstadoCuentaService {

    private final EstadoCuentaPrepagoRepository prepagoRepo;
    private final EstadoCuentaPospagoRepository pospagoRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public EstadoCuentaService(EstadoCuentaPrepagoRepository prepagoRepo, EstadoCuentaPospagoRepository pospagoRepo) {
        this.prepagoRepo = prepagoRepo;
        this.pospagoRepo = pospagoRepo;
    }

    // Ejecutar SP manualmente
    @Transactional
    public void generarEstadosPrepago(LocalDate fechaRef) {
        entityManager.createNativeQuery("CALL sp_generar_estado_cuenta_prepago(:fecha)")
                .setParameter("fecha", fechaRef)
                .executeUpdate();
    }

    @Transactional
    public void generarEstadosPospago(LocalDate fechaRef) {
        entityManager.createNativeQuery("CALL sp_generar_estado_cuenta_pospago(:fecha)")
                .setParameter("fecha", fechaRef)
                .executeUpdate();
    }

    // Programar ejecución automática el primer día del mes a las 02:00 AM
    @Scheduled(cron = "0 0 2 1 * ?")
    public void generarMensual() {
        LocalDate fechaRef = LocalDate.now().withDayOfMonth(1);
        generarEstadosPrepago(fechaRef);
        generarEstadosPospago(fechaRef);
    }

    // Consultas
    public List<EstadoCuentaPrepago> getEstadoCuentaPrepago(Integer idContrato, String periodo) {
        return prepagoRepo.findByIdContratoAndPeriodo(idContrato, periodo);
    }

    public List<EstadoCuentaPospago> getEstadoCuentaPospago(Integer idContrato, String periodo) {
        return pospagoRepo.findByIdContratoAndPeriodo(idContrato, periodo);
    }
}
