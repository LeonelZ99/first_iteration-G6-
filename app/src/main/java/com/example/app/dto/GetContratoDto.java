package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record GetContratoDto(
  Long id,
  LocalDate fechaInicio,
  LocalDate fechaFin,
  String clausulas,
  String estado,
  BigDecimal montoMensual,
  BigDecimal depositoInicial,
  Long idInquilino,
  Long idPropiedad,
  List<Long> idsGarantes
) {}
