package com.example.app.daos;

import com.example.app.model.Contrato;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ContratoDAOO implements IContratoDAO {
  @Override
  public void save(Contrato contrato) {
    String sql = """
      INSERT INTO contratos 
      (fecha_inicio, fecha_fin, clausulas, estado, monto_mensual, deposito_inicial, id_inquilino, id_propiedad) 
      VALUES 
      (:fechaInicio, :fechaFin, :clausulas, :estado, :montoMensual, :depositoInicial, :idInquilino, :idPropiedad)
    """;

    try {
      Sql2oDAO.getSql2o().createQuery(sql)
        .addParameter("fechaInicio", contrato.getFechaInicio())
        .addParameter("fechaFin", contrato.getFechaFin())
        .addParameter("clausulas", contrato.getClausulas())
        .addParameter("estado", contrato.getEstado())
        .addParameter("montoMensual", contrato.getMontoMensual())
        .addParameter("depositoInicial", contrato.getDepositoInicial())
        .addParameter("idInquilino", contrato.getInquilino().getId())
        .addParameter("idPropiedad", contrato.getPropiedad().getId())
        .executeUpdate();
    } catch (org.sql2o.Sql2oException e) {
      e.printStackTrace();
    }
  }
}
