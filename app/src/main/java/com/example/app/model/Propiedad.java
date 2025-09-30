package com.example.app.model;

import java.math.BigDecimal;

// import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class Propiedad {
  private Long id;
  private String direccion;
  private BigDecimal precio;
  private String moneda;
  private String tipoPropiedad;
  private Propietario propietario;
}
