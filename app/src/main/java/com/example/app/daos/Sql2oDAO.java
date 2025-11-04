package com.example.app.daos;

import org.sql2o.Sql2o;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class Sql2oDAO {
  static Sql2o sql2o;

  public static Sql2o getSql2o() {
    if (sql2o == null) {
      sql2o = new Sql2o("jdbc:mysql://localhost:3306/inmobiliaria", "root", "root");
      
      sql2o.setDefaultColumnMappings(
        Map.ofEntries(
          // CONTRATO PARSING
          Map.entry("fecha_inicio", "fechaInicio"),
          Map.entry("fecha_fin", "fechaFin"),
          Map.entry("clausulas", "clausulas"),
          Map.entry("estado", "estado"),
          Map.entry("monto_mensual", "montoMensual"),
          Map.entry("deposito_inicial", "depositoInicial"),
          Map.entry("id_inquilino", "idInquilino"),
          Map.entry("id_propiedad", "idPropiedad"),

          // CLIENTE PARSING
          Map.entry("fecha_nacimiento", "fechaNacimiento"),

          // CAJA PARSING
          Map.entry("tipo_movimiento", "tipoMovimiento"),

          // PROPIETARIO PARSING
          Map.entry("clientes_id", "idCliente"),

          // INQUILINO PARSING
          Map.entry("estado_civil", "estadoCivil"),
          Map.entry("cant_personas_convive", "cantPersonasConvive"),

          // GARANTE PARSING
          Map.entry("contratos_id", "idContrato"),

          // PROPIEDAD PARSING
          Map.entry("tipo_propiedad", "tipoPropiedad"),
          Map.entry("id_propietario", "idPropietario"),

          // PAGOS PARSING
          Map.entry("tipo_pago", "tipoPago"),
          Map.entry("fecha_pago", "fechaPago"),
          Map.entry("id_contrato", "idContrato")
        )
      );
    }
    return sql2o;
  }

  

}
