package com.example.app.dto;

import java.math.BigDecimal;

public record GaranteDto(
  Long idGarante,
  BigDecimal ingresos,
  String trabajo,
  Long idContrato
) {}
