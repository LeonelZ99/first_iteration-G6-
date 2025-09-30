package com.example.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.example.app.db.DBConnection;
import com.example.app.model.Cliente;
import com.example.app.model.Propietario;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;

// import com.example.app.db.DBConnection;
// import com.example.app.model.Propiedad;

// import java.sql.ResultSet;
// import java.util.Optional;

public class PropietarioDAO {
  public Optional<Propietario> getPropietarioById(Long idCliente) {
    String sql = """
      SELECT
        c.id,
        c.nombre,
        c.apellido,
        c.direccion,
        c.fecha_nacimiento,
        c.ingresos,
        c.estado_civil,
        c.telefono,
        c.mail,
        c.dni,
        c.cuil,
        p.cbu
      FROM clientes c
      LEFT JOIN propietarios p ON p.id_propietario = c.id
      WHERE c.id = ?
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

        Propietario p = new Propietario();
        p.setCliente(c);
        p.setCbu(rs.getString("cbu"));

        return Optional.of(p);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
