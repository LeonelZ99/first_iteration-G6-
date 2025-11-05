package com.example.app.daos;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

import com.example.app.model.Inquilino;

@Component
public class InquilinoDAO {

    public Optional<Inquilino> getByClienteId(Long idCliente) {
        String sql = """
                    SELECT
                        estado_civil,
                        ingresos,
                        cant_personas_convive,
                        trabajo,
                        clientes_id
                    FROM inquilinos
                    WHERE clientes_id = :idCliente
                """;

        try (var con = Sql2oDAO.getSql2o().open()) {
            var rowOpt = con.createQuery(sql)
                    .addParameter("idCliente", idCliente)
                    .executeAndFetchTable()
                    .rows()
                    .stream()
                    .findFirst();

            if (rowOpt.isEmpty())
                return Optional.empty();

            Row r = rowOpt.get();
            Inquilino inq = new Inquilino();
            inq.setClientesId(r.getLong("clientes_id"));
            inq.setEstadoCivil(r.getString("estado_civil"));
            inq.setIngresos(r.getBigDecimal("ingresos"));
            inq.setCantPersonasConvive(r.getInteger("cant_personas_convive"));
            inq.setTrabajo(r.getString("trabajo"));
            return Optional.of(inq);
        } catch (org.sql2o.Sql2oException e) {
            System.out.println(e);
            return Optional.empty();
        }
    }
}
