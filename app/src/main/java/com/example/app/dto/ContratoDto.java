package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.app.model.Cliente;
import com.example.app.model.Propiedad;

public record ContratoDto(
  LocalDate fechaInicio,
  LocalDate fechaFin,
  String clausulas,
  String estado,
  BigDecimal montoMensual,
  BigDecimal depositoInicial,
  Cliente inquilino,
  Propiedad propiedad,
  Long idInquilino,
  Long idPropiedad
) {
}
