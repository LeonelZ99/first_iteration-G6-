package com.example.app.daos;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.sql2o.data.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.app.model.Caja;

@Component
public class CajaDAO implements ICajaDAO {

    private static final Logger logger = LoggerFactory.getLogger(CajaDAO.class);

    @Override
    public List<Caja> obtenerTodos() {
        logger.debug("Ejecutando consulta para obtener todos los movimientos de caja");
        String sql = """
                    SELECT
                        id,
                        fecha,
                        tipo_movimiento AS tipoMovimiento,
                        concepto,
                        monto,
                        metodo_pago AS metodoPago,
                        categoria,
                        observaciones
                    FROM caja
                    ORDER BY fecha DESC, id DESC
                """;

        try (var con = Sql2oDAO.getSql2o().open()) {
            List<Row> rows = con.createQuery(sql)
                    .executeAndFetchTable()
                    .rows();

            List<Caja> cajas = new ArrayList<>();
            for (Row row : rows) {
                Caja c = new Caja();
                c.setId(row.getInteger("id"));

                Object fechaObj = row.getObject("fecha");
                if (fechaObj != null) {
                    if (fechaObj instanceof java.sql.Date) {
                        c.setFecha(((java.sql.Date) fechaObj).toLocalDate());
                    } else if (fechaObj instanceof java.sql.Timestamp) {
                        c.setFecha(((java.sql.Timestamp) fechaObj).toLocalDateTime().toLocalDate());
                    }
                }

                c.setTipoMovimiento(row.getString("tipoMovimiento"));
                c.setConcepto(row.getString("concepto"));
                c.setMonto(row.getBigDecimal("monto"));
                c.setMetodoPago(row.getString("metodoPago"));
                c.setCategoria(row.getString("categoria"));
                c.setObservaciones(row.getString("observaciones"));
                cajas.add(c);
            }
            logger.info("Se obtuvieron {} movimientos de caja", cajas.size());
            return cajas;
        } catch (org.sql2o.Sql2oException e) {
            logger.error("Error al obtener movimientos de caja", e);
            return new ArrayList<>();
        }
    }
}