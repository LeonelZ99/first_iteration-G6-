package com.example.app.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inquilino {
  private Long clientesId;
  private String estadoCivil;
  private BigDecimal ingresos;
  private Integer cantPersonasConvive;
  private String trabajo;
}
