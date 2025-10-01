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
        id            
        direccion     
        precio        
        moneda        
        tipo_propiedad
        id_propietario

      FROM propiedades
      WHERE id = :idPropiedad
    """;
    
    try {
      Row sqlResponse = Sql2oDAO.getSql2o().createQuery(sql)
        .addParameter("idPropiedad", idPropiedad)
        .executeAndFetchTable()
        .rows()
        .stream()
        .findFirst()
        .get();

      if(sqlResponse.getLong("id") == null) {
        return Optional.empty();
      } else {
        
        Propiedad propiedad = new Propiedad();
        propiedad.setId(sqlResponse.getLong("id"));
        propiedad.setDireccion(sqlResponse.getString("direccion"));
        propiedad.setPrecio(sqlResponse.getBigDecimal("precio"));
        propiedad.setMoneda(sqlResponse.getString("tipo"));
        propiedad.setMoneda(sqlResponse.getString("moneda"));
        propiedad.setIdPropietario(sqlResponse.getLong("id_propietario"));

        return Optional.of(propiedad);
      }
    } catch (org.sql2o.Sql2oException e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
