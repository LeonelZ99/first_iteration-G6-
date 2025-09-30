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
      Row row = Sql2oDAO.getSql2o()
        .open()
        .createQuery(sql)
        .addParameter("idCliente", idCliente)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst()
        .orElse(null);

      if (row == null) return Optional.empty();

      Cliente c = new Cliente();
      c.setId(row.getLong("id"));
      c.setNombre(row.getString("nombre"));
      c.setApellido(row.getString("apellido"));
      c.setDireccion(row.getString("direccion"));
      c.setFechaNacimiento(((java.sql.Date) row.getDate("fecha_nacimiento")).toLocalDate());
      c.setIngresos(row.getBigDecimal("ingresos"));
      c.setEstadoCivil(row.getString("estado_civil"));
      c.setTelefono(row.getString("telefono"));
      c.setMail(row.getString("mail"));
      c.setDni(row.getString("dni"));
      c.setCuil(row.getString("cuil"));

      Propietario p = new Propietario();
      p.setCliente(c);
      p.setCbu(row.getString("cbu"));

      return Optional.of(p);

    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
