package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class Pago {
  private Long id;
  private String tipoPago;
  private LocalDate fechaPago;
  private BigDecimal monto;
  private BigDecimal expensas;
  private Long idContrato;
}
