package com.example.app.daos;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

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
      WHERE id = :idCliente
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

      return Optional.of(c);

    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
