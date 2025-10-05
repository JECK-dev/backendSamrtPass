package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Cliente;
import com.smartpass.smartpassbackend.model.Contrato;
import com.smartpass.smartpassbackend.model.Vehiculo;
import com.smartpass.smartpassbackend.repository.CategoriaRepository;
import com.smartpass.smartpassbackend.repository.ContratoRepository;
import com.smartpass.smartpassbackend.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class NotificacionService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EmailService emailService;

    // Ejecuta cada hora (ajusta a fixedRate = 300000 para 5 minutos si quieres probar)
    @Scheduled(fixedRate = 300000)
    public void verificarSaldos() {
        System.out.println("🕒 Verificando saldos... " + java.time.LocalTime.now());

        List<Contrato> contratosPrepago = contratoRepository.findByTipo("PRE");
        System.out.println("🔍 Contratos encontrados: " + contratosPrepago.size());

        for (Contrato contrato : contratosPrepago) {
            BigDecimal saldo = contrato.getSaldo();
            if (saldo == null) continue;

            // Vehículos asociados al contrato
            List<Vehiculo> vehiculos = vehiculoRepository.findByContratoId(contrato.getIdContrato());
            if (vehiculos == null || vehiculos.isEmpty()) continue;

            // Tomar el id de categoría más alto entre los vehículos del contrato
            // (asumiendo que Vehiculo tiene un campo Integer categoria que representa id_categoria)
            Integer idCategoriaMax = vehiculos.stream()
                    .map(Vehiculo::getCategoria)              // -> Integer (id_categoria)
                    .filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            if (idCategoriaMax == null) continue;

            // Buscar el peaje de esa categoría (BigDecimal)
            BigDecimal montoPeajeMax = categoriaRepository.findMontoPeajeByIdCategoria(idCategoriaMax);
            if (montoPeajeMax == null) continue;

            // Si saldo < peaje -> notificar
            if (saldo.compareTo(montoPeajeMax) < 0) {
                Cliente cliente = contrato.getCliente();
                if (cliente != null && cliente.getCorreo() != null && !cliente.getCorreo().isBlank()) {
                    enviarAlerta(cliente, saldo, montoPeajeMax);
                }
            }
        }
    }

    private void enviarAlerta(Cliente cliente, BigDecimal saldo, BigDecimal peajeNecesario) {
        String asunto = "⚠️ Saldo insuficiente en SmartPass";
        // Mostrar los valores tal cual, sin redondeo (toPlainString evita notación científica)
        String mensaje = "Hola " + cliente.getNombre() + ",\n\n" +
                "Tu saldo actual es: S/ " + saldo.toPlainString() + ".\n" +
                "El peaje más alto de tus vehículos cuesta: S/ " + peajeNecesario.toPlainString() + ".\n\n" +
                "Por favor recarga para evitar bloqueos.\n\n" +
                "Atentamente,\nSmartPass 🚗💨";

        emailService.enviarCorreo(cliente.getCorreo(), asunto, mensaje);
    }
}
