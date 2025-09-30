package com.example.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.app.db.DBConnection;
import com.example.app.model.Cliente;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ClienteDAO implements IClienteDAO {
  @Override
  public Optional<Cliente> getClienteById(Long idCliente) {
    String sql = """
      SELECT id, nombre, apellido, direccion, fecha_nacimiento, ingresos,
             estado_civil, telefono, mail, dni, cuil
      FROM clientes
      WHERE id = ?
    """;

    try (
      Connection conn = DBConnection.getInstance().getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
      stmt.setLong(1, idCliente);

      ResultSet rs = stmt.executeQuery();

      if(rs.next()) {
        Cliente c = new Cliente();

        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setDireccion(rs.getString("direccion"));
        c.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        c.setIngresos(rs.getBigDecimal("ingresos"));
        c.setEstadoCivil(rs.getString("estado_civil"));
        c.setTelefono(rs.getString("telefono"));
        c.setMail(rs.getString("mail"));
        c.setDni(rs.getString("dni"));
        c.setCuil(rs.getString("cuil"));

        return Optional.of(c);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
