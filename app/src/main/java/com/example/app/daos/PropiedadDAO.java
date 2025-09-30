package com.example.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.example.app.db.DBConnection;

import com.example.app.model.Propiedad;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PropiedadDAO implements IPropiedadDAO {
  @Override
  public Optional<Propiedad> getPropiedadById(Long idPropiedad) {
    String sql = """
      SELECT id, direccion, precio, moneda, tipo_propiedad, id_propietario
      FROM propiedades
      WHERE id = ?
    """;
    
    try (
      Connection conn = DBConnection.getInstance().getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {

      stmt.setLong(1, idPropiedad);

      ResultSet rs = stmt.executeQuery();

      Propiedad p = new Propiedad();
      p.setId(rs.getLong("id"));
      p.setDireccion(rs.getString("direccion"));
      p.setPrecio(rs.getBigDecimal("precio"));
      p.setMoneda(rs.getString("moneda"));
      p.setTipoPropiedad(rs.getString("tipo_propiedad"));

      return Optional.of(p);
    } catch (SQLException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
