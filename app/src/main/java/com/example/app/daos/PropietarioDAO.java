package com.example.app.daos;

import java.time.ZoneId;
import java.util.Optional;

import org.sql2o.data.Row;

import com.example.app.model.Cliente;
import com.example.app.model.Propietario;

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
      WHERE c.id = :idCliente
    """;

    try {
      Optional<Row> row = Sql2oDAO.getSql2o()
        .open()
        .createQuery(sql)
        .addParameter("idCliente", idCliente)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst();

      if (row.isEmpty()) return Optional.empty();

      Row sqlResponse = row.get();

      Cliente c = new Cliente();
      c.setId(sqlResponse.getLong("id"));
      c.setNombre(sqlResponse.getString("nombre"));
      c.setApellido(sqlResponse.getString("apellido"));
      c.setDireccion(sqlResponse.getString("direccion"));
      c.setFechaNacimiento(((java.sql.Date) sqlResponse.getDate("fecha_nacimiento")).toLocalDate());
      c.setTelefono(sqlResponse.getString("telefono"));
      c.setMail(sqlResponse.getString("mail"));
      c.setDni(sqlResponse.getString("dni"));
      c.setCuil(sqlResponse.getString("cuil"));

      Propietario p = new Propietario();
      // p.setCliente(c);
      p.setCbu(sqlResponse.getString("cbu"));

      return Optional.of(p);

    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
