package com.example.app.daos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

import com.example.app.dto.BalanceDto;
import com.example.app.model.Caja;

@Component
public class CajaDAO implements ICajaDAO {

    @Override
    public List<Caja> obtenerPorPeriodo(LocalDate desde, LocalDate hasta, String tipoMovimiento) {
        String sql = """
                    SELECT
                        id,
                        fecha,
                        tipo_movimiento AS tipoMovimiento,
                        concepto,
                        monto,
                        metodo_pago AS metodoPago,
                        categoria,
                        observaciones,
                        usuario,
                        propiedades_id AS propiedadesId,
                        contratos_id AS contratosId
                    FROM caja
                    WHERE fecha BETWEEN :desde AND :hasta
                      AND (:tipoMovimiento IS NULL OR tipo_movimiento = :tipoMovimiento)
                    ORDER BY fecha ASC, id ASC
                """;

        try (var con = Sql2oDAO.getSql2o().open()) {
            List<Row> rows = con.createQuery(sql)
                    .addParameter("desde", desde)
                    .addParameter("hasta", hasta)
                    .addParameter("tipoMovimiento", tipoMovimiento)
                    .executeAndFetchTable()
                    .rows();

            List<Caja> cajas = new ArrayList<>();
            for (Row row : rows) {
                Caja c = new Caja();
                c.setId(row.getInteger("id"));
                c.setFecha(((java.sql.Date) row.getDate("fecha")).toLocalDate());
                c.setTipoMovimiento(row.getString("tipoMovimiento"));
                c.setConcepto(row.getString("concepto"));
                c.setMonto(row.getBigDecimal("monto"));
                c.setMetodoPago(row.getString("metodoPago"));
                c.setCategoria(row.getString("categoria"));
                c.setObservaciones(row.getString("observaciones"));
                c.setUsuario(row.getString("usuario"));
                c.setPropiedadesId(row.getInteger("propiedadesId"));
                c.setContratosId(row.getInteger("contratosId"));
                cajas.add(c);
            }
            return cajas;
        } catch (org.sql2o.Sql2oException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public BalanceDto calcularBalance(LocalDate desde, LocalDate hasta, int mes, int anio) {
        String sql = """
                    SELECT
                        COALESCE(SUM(CASE WHEN tipo_movimiento = 'INGRESO' THEN monto ELSE 0 END), 0) AS totalIngresos,
                        COALESCE(SUM(CASE WHEN tipo_movimiento = 'EGRESO'  THEN monto ELSE 0 END), 0) AS totalEgresos
                    FROM caja
                    WHERE fecha BETWEEN :desde AND :hasta
                """;

        try (var con = Sql2oDAO.getSql2o().open()) {
            var table = con.createQuery(sql)
                    .addParameter("desde", desde)
                    .addParameter("hasta", hasta)
                    .executeAndFetchTable();

            var row = table.rows().get(0);
            BigDecimal totalIngresos = row.getBigDecimal("totalIngresos");
            BigDecimal totalEgresos = row.getBigDecimal("totalEgresos");
            BigDecimal balance = totalIngresos.subtract(totalEgresos);

            return new BalanceDto(totalIngresos, totalEgresos, balance, mes, anio);
        } catch (org.sql2o.Sql2oException e) {
            return new BalanceDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, mes, anio);
        }
    }
}