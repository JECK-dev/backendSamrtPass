package com.smartpass.smartpassbackend.service;

import com.smartpass.smartpassbackend.repository.ContratoRepository;
import com.smartpass.smartpassbackend.repository.EstadoCuentaDao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadoCuentaService {

    private final EstadoCuentaDao dao;              // <-- usamos DAO
    private final ContratoRepository contratoRepo;  // este sí puede seguir como Spring Data JPA

    public EstadoCuentaService(EstadoCuentaDao dao, ContratoRepository contratoRepo) {
        this.dao = dao;
        this.contratoRepo = contratoRepo;
    }

    public List<Map<String, Object>> listarContratos(Integer clienteId) {
        var rows = contratoRepo.listarPorClienteActivo(clienteId);
        List<Map<String, Object>> out = new ArrayList<>();
        for (var r : rows) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getIdContrato());
            m.put("codigo", r.getnroContrato());
            out.add(m);
        }
        return out;
    }

    public Map<String, Object> obtenerEstadoCuenta(Integer contratoId,
                                                   YearMonth periodo,
                                                   String buscar,
                                                   int page,
                                                   int size) {
        LocalDate inicio = periodo.atDay(1);
        LocalDate fin = inicio.plusMonths(1);
        int limit  = Math.max(1, size);
        int offset = Math.max(0, page) * limit;

        BigDecimal saldoInicial = dao.saldoInicial(contratoId, inicio);

        var movs = dao.movimientos(
                contratoId, inicio, fin,
                (buscar == null || buscar.isBlank()) ? null : buscar,
                offset, limit
        );

        var tot = dao.totales(contratoId, inicio, fin);
        BigDecimal cargos = (BigDecimal) tot.get("cargos");
        BigDecimal abonos = (BigDecimal) tot.get("abonos");
        BigDecimal saldoFinal = saldoInicial.add(abonos).subtract(cargos);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("contratoId", contratoId);
        body.put("periodo", periodo.toString());
        body.put("saldoInicial", saldoInicial);
        body.put("cargos", cargos);
        body.put("abonos", abonos);
        body.put("saldoFinal", saldoFinal);
        body.put("movimientos", movs);
        return body;
    }
}
