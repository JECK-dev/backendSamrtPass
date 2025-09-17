package com.smartpass.smartpassbackend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EstadoCuentaDao {

    private final JdbcTemplate jdbc;

    public EstadoCuentaDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public BigDecimal saldoInicial(Integer contratoId, LocalDate inicio) {
        return jdbc.queryForObject(
                "SELECT COALESCE(SUM(monto),0) " +
                        "FROM vw_movimientos_contrato " +
                        "WHERE id_contrato = ? AND fecha < ?",
                BigDecimal.class, contratoId, Date.valueOf(inicio)
        );
    }

    public List<Map<String, Object>> movimientos(Integer contratoId,
                                                 LocalDate inicio,
                                                 LocalDate fin,
                                                 String buscar,
                                                 int offset,
                                                 int limit) {
        String baseSql =
                "SELECT fecha, tipo, descripcion, monto " +
                        "FROM vw_movimientos_contrato " +
                        "WHERE id_contrato = ? AND fecha >= ? AND fecha < ? ";

        List<Object> args = new ArrayList<>(List.of(contratoId, Date.valueOf(inicio), Date.valueOf(fin)));

        if (buscar != null && !buscar.isBlank()) {
            baseSql += "AND (descripcion ILIKE CONCAT('%', ?, '%') OR tipo ILIKE CONCAT('%', ?, '%')) ";
            args.add(buscar);
            args.add(buscar);
        }

        baseSql += "ORDER BY fecha ASC OFFSET ? LIMIT ? ";
        args.add(offset);
        args.add(limit);

        return jdbc.query(baseSql, args.toArray(), (rs, i) -> {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("fecha", rs.getDate("fecha").toLocalDate()); // ISO friendly
            m.put("tipo", rs.getString("tipo"));
            m.put("descripcion", rs.getString("descripcion"));
            m.put("monto", rs.getBigDecimal("monto"));
            return m;
        });
    }

    public Map<String, Object> totales(Integer contratoId, LocalDate inicio, LocalDate fin) {
        return jdbc.queryForMap(
                "SELECT " +
                        "COALESCE(SUM(CASE WHEN monto < 0 THEN -monto ELSE 0 END),0) AS cargos, " +
                        "COALESCE(SUM(CASE WHEN monto > 0 THEN  monto ELSE 0 END),0) AS abonos " +
                        "FROM vw_movimientos_contrato " +
                        "WHERE id_contrato = ? AND fecha >= ? AND fecha < ?",
                contratoId, Date.valueOf(inicio), Date.valueOf(fin)
        );
    }
}