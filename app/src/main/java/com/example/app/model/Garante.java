package com.example.app.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Garante {
  private BigDecimal ingresos;
  private String trabajo;
  private Long idCliente;
  private Long idContrato;
}