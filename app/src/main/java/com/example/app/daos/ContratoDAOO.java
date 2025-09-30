package com.example.app.daos;

import java.sql.PreparedStatement;

import com.example.app.db.DBConnection;
import com.example.app.model.Contrato;
import java.sql.Connection;
import java.sql.SQLException;

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
      (?, ?, ?, ?, ?, ?, ?, ?)
    """;
    
    try (
      Connection conn = DBConnection.getInstance().getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
      // stmt.setLong(1, 1);
      stmt.setDate(2, java.sql.Date.valueOf(contrato.getFechaInicio()));
      stmt.setDate(3, java.sql.Date.valueOf(contrato.getFechaFin())); 
      stmt.setString(4, contrato.getClausulas());
      stmt.setString(5, contrato.getEstado());
      stmt.setBigDecimal(6, contrato.getMontoMensual());
      stmt.setBigDecimal(7, contrato.getDepositoInicial());
      stmt.setLong(8, contrato.getInquilino().getId());
      stmt.setLong(9, contrato.getPropiedad().getId());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
