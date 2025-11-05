package com.example.app.daos;

import java.util.Optional;

import org.sql2o.data.Row;
import org.springframework.stereotype.Component;

import com.example.app.model.Cliente;
import com.example.app.model.Propietario;

@Component
public class PropietarioDAO {
  public Optional<Propietario> getPropietarioById(Long idCliente) {
    String sql = """
          SELECT
            c.id,
            c.nombre,
            c.apellido,
            c.direccion,
            c.fecha_nacimiento,
            c.telefono,
            c.mail,
            c.dni,
            c.cuil,
            p.cbu,
            p.clientes_id
          FROM clientes c
          JOIN propietarios p ON p.clientes_id = c.id
          WHERE c.id = :idCliente
        """;

    try (var con = Sql2oDAO.getSql2o().open()) {
      Optional<Row> rowOpt = con.createQuery(sql)
          .addParameter("idCliente", idCliente)
          .executeAndFetchTable()
          .rows()
          .stream()
          .findFirst();

      if (rowOpt.isEmpty())
        return Optional.empty();

      Row r = rowOpt.get();

      Cliente c = new Cliente();
      c.setId(r.getLong("id"));
      c.setNombre(r.getString("nombre"));
      c.setApellido(r.getString("apellido"));
      c.setDireccion(r.getString("direccion"));
      c.setFechaNacimiento(((java.sql.Date) r.getDate("fecha_nacimiento")).toLocalDate());
      c.setTelefono(r.getString("telefono"));
      c.setMail(r.getString("mail"));
      c.setDni(r.getString("dni"));
      c.setCuil(r.getString("cuil"));

      Propietario p = new Propietario();
      p.setClientesId(r.getLong("clientes_id"));
      p.setCbu(r.getString("cbu"));
      // si quisieras, podrías guardar el Cliente dentro de Propietario
      // p.setCliente(c);

      return Optional.of(p);

    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
