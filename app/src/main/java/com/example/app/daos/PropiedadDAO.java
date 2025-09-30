package com.example.app.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.example.app.db.DBConnection;
import com.example.app.model.Cliente;
import com.example.app.model.Propiedad;
import com.example.app.model.Propietario;

import org.springframework.stereotype.Component;

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
      WHERE pr.id = ?
    """;
    
    try (
      Connection conn = DBConnection.getInstance().getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql)
    ) {

      stmt.setLong(1, idPropiedad);

      ResultSet rs = stmt.executeQuery();
      if(rs.next()) {
        Propiedad propiedad = new Propiedad();
        propiedad.setId(rs.getLong("prop_id"));
        propiedad.setDireccion(rs.getString("prop_direccion"));
        propiedad.setPrecio(rs.getBigDecimal("prop_precio"));
        propiedad.setMoneda(rs.getString("prop_moneda"));
        propiedad.setTipoPropiedad(rs.getString("prop_tipo"));

        Cliente c = new Cliente();

        c.setId(rs.getLong("cli_id"));
        c.setNombre(rs.getString("cli_nombre"));
        c.setApellido(rs.getString("cli_apellido"));
        c.setDireccion(rs.getString("cli_direccion"));
        c.setFechaNacimiento(rs.getDate("cli_fecha_nac").toLocalDate());
        c.setIngresos(rs.getBigDecimal("cli_ingresos"));
        c.setEstadoCivil(rs.getString("cli_estado_civil"));
        c.setTelefono(rs.getString("cli_telefono"));
        c.setMail(rs.getString("cli_mail"));
        c.setDni(rs.getString("cli_dni"));
        c.setCuil(rs.getString("cli_cuil"));

        Propietario propietario = new Propietario();
        propietario.setCliente(c);
        propietario.setCbu(rs.getString("prop_cbu"));

        propiedad.setPropietario(propietario);

        System.out.println(propiedad);

        return Optional.of(propiedad);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
