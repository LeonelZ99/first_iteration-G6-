package com.example.app.daos;

import java.time.ZoneId;
import java.util.Optional;

import com.example.app.model.Cliente;
import com.example.app.model.Propiedad;
import com.example.app.model.Propietario;

import org.springframework.stereotype.Component;
import org.sql2o.data.Row;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PropiedadDAO implements IPropiedadDAO {
  @Override
  public Optional<Propiedad> getPropiedadById(Long idPropiedad) {
    String sql = """
      SELECT
        pr.id                AS prop_id,
        pr.direccion         AS prop_direccion,
        pr.precio            AS prop_precio,
        pr.moneda            AS prop_moneda,
        pr.tipo_propiedad    AS prop_tipo,
        pr.id_propietario    AS prop_id_propietario,

        p.cbu                AS prop_cbu,

        c.id                 AS cli_id,
        c.nombre             AS cli_nombre,
        c.apellido           AS cli_apellido,
        c.direccion          AS cli_direccion,
        c.fecha_nacimiento   AS cli_fecha_nac,
        c.ingresos           AS cli_ingresos,
        c.estado_civil       AS cli_estado_civil,
        c.telefono           AS cli_telefono,
        c.mail               AS cli_mail,
        c.dni                AS cli_dni,
        c.cuil               AS cli_cuil

      FROM propiedades pr
      LEFT JOIN propietarios p ON p.id_propietario = pr.id_propietario
      LEFT JOIN clientes     c ON c.id            = pr.id_propietario
      WHERE pr.id = :idPropiedad
    """;
    
    try {
      Row sqlResponse = Sql2oDAO.getSql2o().createQuery(sql)
        .addParameter("idPropiedad", idPropiedad)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst()
        .get();

      if(sqlResponse.getLong("prop_id") == null) {
        return Optional.empty();
      } else {
        
        Propiedad propiedad = new Propiedad();
        propiedad.setId(sqlResponse.getLong("prop_id"));
        propiedad.setDireccion(sqlResponse.getString("prop_direccion"));
        propiedad.setPrecio(sqlResponse.getBigDecimal("prop_precio"));
        propiedad.setMoneda(sqlResponse.getString("prop_moneda"));
        propiedad.setTipoPropiedad(sqlResponse.getString("prop_tipo"));

        Cliente c = new Cliente();

        c.setId(sqlResponse.getLong("cli_id"));
        c.setNombre(sqlResponse.getString("cli_nombre"));
        c.setApellido(sqlResponse.getString("cli_apellido"));
        c.setDireccion(sqlResponse.getString("cli_direccion"));
        c.setFechaNacimiento(((java.sql.Date) sqlResponse.getDate("cli_fecha_nac")).toLocalDate());
        c.setIngresos(sqlResponse.getBigDecimal("cli_ingresos"));
        c.setEstadoCivil(sqlResponse.getString("cli_estado_civil"));
        c.setTelefono(sqlResponse.getString("cli_telefono"));
        c.setMail(sqlResponse.getString("cli_mail"));
        c.setDni(sqlResponse.getString("cli_dni"));
        c.setCuil(sqlResponse.getString("cli_cuil"));

        Propietario propietario = new Propietario();
        propietario.setCliente(c);
        propietario.setCbu(sqlResponse.getString("prop_cbu"));

        propiedad.setPropietario(propietario);


        return Optional.of(propiedad);

      }
    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
