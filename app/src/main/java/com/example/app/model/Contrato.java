package com.example.app.model;

// import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Contrato {
  private Long id;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private String clausulas;
  private String estado;
  private BigDecimal montoMensual;
  private BigDecimal depositoInicial;
  private Cliente inquilino;
  private Propiedad propiedad;
  // private List<Pago> pagos;
}
