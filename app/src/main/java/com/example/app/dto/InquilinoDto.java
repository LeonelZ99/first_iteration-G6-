package com.example.app.dto;

import java.math.BigDecimal;

public record InquilinoDto(
  String estadoCivil, 
  BigDecimal ingresos,
  Integer cantPersonasConvive,
  String trabajo,
  Long idCliente
) {}
