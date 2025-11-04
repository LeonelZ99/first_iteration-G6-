package com.example.app.daos;

import com.example.app.model.Cliente;
import com.example.app.model.Garante;
import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GaranteDAO extends CrudDAO<Garante, Long> {

  @Override public Class<Garante> getTClass()   { return Garante.class; }
  @Override public String        getTablePK()    { return "idCliente"; }
  @Override public String        getTableName()  { return "garantes"; } // o "inmobiliaria.contratos"
  
  @Override
  protected Map<String, String> getColumnMappings() {
    return Map.of(
      "ingresos", "ingresos",
      "trabajo", "trabajo",
      "idCliente", "clientes_id",
      "idContrato", "contratos_id"
    );
  }

  public Optional<Garante> getGaranteById(Long idCliente) {
    return findById(idCliente);
  }

  public List<Garante> getGarantesByIdContrato(Long idContrato) {
    String col = resolveColumn("idContrato");

    String sql = "SELECT * FROM " + getTableName() + " WHERE " + col + " = :idContrato";

    try (var con = sql2o().open()) {
      return con.createQuery(sql)
        .addParameter("idContrato", idContrato)
        .executeAndFetch(getTClass());
    }
  }
}