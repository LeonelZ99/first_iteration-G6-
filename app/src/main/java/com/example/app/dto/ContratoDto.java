package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoDto(
  LocalDate fechaInicio,
  LocalDate fechaFin,
  String clausulas,
  String estado,
  BigDecimal montoMensual,
  BigDecimal depositoInicial,
  Long idInquilino,
  Long idPropiedad
) {
}
