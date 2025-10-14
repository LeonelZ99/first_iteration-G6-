package com.example.app.daos;

import com.example.app.model.Contrato;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

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
        .addParameter("idInquilino", contrato.getIdInquilino())
        .addParameter("idPropiedad", contrato.getIdPropiedad())
        .executeUpdate();
    } catch (org.sql2o.Sql2oException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Optional<Contrato> getContratoById(Long idContrato) {
    String sql = """
      SELECT
        id,
        fecha_inicio,
        fecha_fin,
        clausulas,
        estado,
        monto_mensual,
        deposito_inicial,
        id_inquilino,
        id_propiedad
      FROM inmobiliaria.contratos
      WHERE id = :idContrato;
    """;
    
    try {
      Optional<Row> row = Sql2oDAO.getSql2o().createQuery(sql)
        .addParameter("idContrato", idContrato)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst();

      if(row.isEmpty()) {
        return Optional.empty();
      } else {
        Row sqlResponse = row.get();

        Contrato contrato = new Contrato();
        contrato.setId(sqlResponse.getLong("id"));
        contrato.setFechaInicio(((java.sql.Date) sqlResponse.getDate("fecha_inicio")).toLocalDate());
        contrato.setFechaFin(((java.sql.Date) sqlResponse.getDate("fecha_fin")).toLocalDate());
        contrato.setClausulas(sqlResponse.getString("clausulas"));
        contrato.setEstado(sqlResponse.getString("estado"));
        contrato.setMontoMensual(sqlResponse.getBigDecimal("monto_mensual"));
        contrato.setDepositoInicial(sqlResponse.getBigDecimal("deposito_inicial"));
        contrato.setIdInquilino(sqlResponse.getLong("id_inquilino"));
        contrato.setIdPropiedad(sqlResponse.getLong("id_propiedad"));

        return Optional.of(contrato);
      }
    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }

  @Override 
  public Optional<Contrato> getContratoByIdInquilino(Long idInquilino) {
      String sql = """
      SELECT
        id,
        fecha_inicio,
        fecha_fin,
        clausulas,
        estado,
        monto_mensual,
        deposito_inicial,
        id_inquilino,
        id_propiedad
      FROM contrato
      WHERE id_inquilino = :idInquilino
    """;
    
    try {
      Optional<Row> row = Sql2oDAO.getSql2o().createQuery(sql)
        .addParameter("idInquilino", idInquilino)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst();

      if(row.isEmpty()) {
        return Optional.empty();
      } else {
        Row sqlResponse = row.get();

        Contrato contrato = new Contrato();
        contrato.setId(sqlResponse.getLong("id"));
        contrato.setFechaInicio(((java.sql.Date) sqlResponse.getDate("fecha_inicio")).toLocalDate());
        contrato.setFechaFin(((java.sql.Date) sqlResponse.getDate("fecha_fin")).toLocalDate());
        contrato.setClausulas(sqlResponse.getString("clausulas"));
        contrato.setEstado(sqlResponse.getString("estado"));
        contrato.setMontoMensual(sqlResponse.getBigDecimal("monto_mensual"));
        contrato.setDepositoInicial(sqlResponse.getBigDecimal("deposito_inicial"));
        contrato.setIdInquilino(sqlResponse.getLong("id_inquilino"));
        contrato.setIdPropiedad(sqlResponse.getLong("id_propiedad"));

        return Optional.of(contrato);
      }
    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
