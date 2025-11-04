package com.example.app.daos;

import com.example.app.model.Contrato;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ContratoDAOO extends CrudDAO<Contrato, Long> {

  @Override public Class<Contrato> getTClass()   { return Contrato.class; }
  @Override public String        getTablePK()    { return "id"; }
  @Override public String        getTableName()  { return "contratos"; } // o "inmobiliaria.contratos"

  @Override
  protected Map<String, String> getColumnMappings() {
    return Map.of(
      "fechaInicio", "fecha_inicio",
      "fechaFin", "fecha_fin",
      "montoMensual", "monto_mensual",
      "depositoInicial", "deposito_inicial",
      "idInquilino", "id_inquilino",
      "idPropiedad", "id_propiedad"
    );
  }

  public Optional<Contrato> getContratoById(Long idContrato) {
    return findById(idContrato);
  }
}

