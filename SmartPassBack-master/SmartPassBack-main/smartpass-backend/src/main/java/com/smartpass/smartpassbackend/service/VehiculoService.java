package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.model.Cliente;
import com.smartpass.smartpassbackend.model.Vehiculo;
import com.smartpass.smartpassbackend.repository.VehiculoRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    // Listar vehículos por cliente
    public List<Vehiculo> obtenerVehiculosPorCliente(Long idCliente) {
        return vehiculoRepository.findByCliente_IdCliente(idCliente);
    }

    public List<Vehiculo> obtenerVehiculosPorDocumento(String documento) {
        return vehiculoRepository.findByCliente_NumDocumento(documento);
    }

    public void desafiliarVehiculo(Long idVehiculo) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        vehiculo.setIdEstado(2); // 2 = desafiliado
        vehiculoRepository.save(vehiculo);
    }



    public Optional<Vehiculo> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }

    public Vehiculo guardar(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo actualizar(Integer id, Vehiculo vehiculo) {
        vehiculo.setIdVehiculo(id);
        return vehiculoRepository.save(vehiculo);
    }


    public String insertarVehiculo(String placa, Integer idContrato, Integer categoria,
                                   String modelo, String color, String marca, Long cliente) {

        if (cliente == null ) {
            return "ERROR: El cliente es nulo o no tiene idCliente";
        }

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_insertar_vehiculo");

        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(7, Integer.class, ParameterMode.IN);

        query.setParameter(1, placa);
        query.setParameter(2, idContrato);
        query.setParameter(3, categoria);
        query.setParameter(4, modelo);
        query.setParameter(5, color);
        query.setParameter(6, marca);
        query.setParameter(7, cliente ); // 👈 aquí usamos el ID, no el objeto

        query.execute();
        return "OK";
    }






}