package com.example.app.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Inquilino {
  private String estadoCivil; 
  private BigDecimal ingresos;
  private Integer cantPersonasConvive;
  private String trabajo;
  private Long idCliente;
}
