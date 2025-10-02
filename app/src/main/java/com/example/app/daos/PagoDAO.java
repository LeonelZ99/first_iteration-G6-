package com.example.app.daos;

import com.example.app.dto.PagoDto;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PagoDAO implements IPagoDAO {

    private final Sql2o sql2o;

    public PagoDAO() {
        this.sql2o = Sql2oDAO.getSql2o();
    }

    @Override
    public boolean existsPago(Long contratoId, String periodo) {
        String sql = """
                  SELECT 1
                  FROM pagos
                  WHERE contrato_id = :contratoId
                    AND periodo = :periodo
                  LIMIT 1
                """;
        try (Connection con = sql2o.open()) {
            Integer flag = con.createQuery(sql)
                    .addParameter("contratoId", contratoId)
                    .addParameter("periodo", periodo)
                    .executeAndFetchFirst(Integer.class);
            return flag != null;
        }
    }

    @Override
    public Long insertPago(Long contratoId, String periodo, LocalDate fechaPago, String tipoPago,
            BigDecimal expensas, BigDecimal impuestos, BigDecimal monto) {
        String sql = """
                  INSERT INTO pagos (contrato_id, periodo, fecha_pago, tipo_pago, expensas, impuestos, monto)
                  VALUES (:contratoId, :periodo, :fechaPago, :tipoPago, :expensas, :impuestos, :monto)
                """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql, true)
                    .addParameter("contratoId", contratoId)
                    .addParameter("periodo", periodo)
                    .addParameter("fechaPago", fechaPago)
                    .addParameter("tipoPago", tipoPago)
                    .addParameter("expensas", expensas)
                    .addParameter("impuestos", impuestos)
                    .addParameter("monto", monto)
                    .executeUpdate()
                    .getKey(Long.class);
        }
    }

    @Override
    public List<PagoDto> findPagosByContrato(Long contratoId) {
        String sql = """
                  SELECT id, contrato_id AS contratoId, periodo, fecha_pago AS fechaPago, tipo_pago AS tipoPago,
                         expensas, impuestos, monto
                  FROM pagos
                  WHERE contrato_id = :contratoId
                  ORDER BY periodo DESC
                """;
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("contratoId", contratoId)
                    .executeAndFetch(PagoDto.class);
        }
    }
}
