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
                  WHERE id_contrato = :contratoId
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
                  INSERT INTO pagos (id_contrato, periodo, fecha_pago, tipo_pago, expensas, impuestos, monto)
                  VALUES (:contratoId, :periodo, :fechaPago, :tipoPago, :expensas, :impuestos, :monto)
                """;
        try (Connection con = sql2o.beginTransaction()) {
            Long id = con.createQuery(sql, true)
                    .addParameter("contratoId", contratoId)
                    .addParameter("periodo", periodo)
                    .addParameter("fechaPago", fechaPago)
                    .addParameter("tipoPago", tipoPago)
                    .addParameter("expensas", expensas)
                    .addParameter("impuestos", impuestos)
                    .addParameter("monto", monto)
                    .executeUpdate()
                    .getKey(Long.class);
            con.commit();
            return id;
        } catch (Exception e) {
            if (isDuplicateKey(e)) {
                throw new RuntimeException("El período ya está cancelado (duplicado contrato+período)", e);

            }
            throw e;
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

    private boolean isDuplicateKey(Throwable t) {
        Throwable c = t;
        while (c != null) {
            if (c instanceof java.sql.SQLException sqlEx) {
                // MySQL: SQLState 23000 y errorCode 1062
                if ("23000".equals(sqlEx.getSQLState()) || sqlEx.getErrorCode() == 1062)
                    return true;
            }
            c = c.getCause();
        }
        return false;
    }

    public java.util.Optional<Long> findIdByContratoPeriodo(Long contratoId, String periodo) {
        String sql = """
                SELECT id
                FROM pagos
                WHERE contrato_id = :contratoId AND periodo = :periodo
                LIMIT 1
                """;
        try (Connection con = sql2o.open()) {
            Long id = con.createQuery(sql)
                    .addParameter("contratoId", contratoId)
                    .addParameter("periodo", periodo)
                    .executeAndFetchFirst(Long.class);
            return java.util.Optional.ofNullable(id);
        }
    }
}
