package com.example.app.dto;

import com.example.app.model.Contrato;

public record ContratoDto(
  Contrato contrato,
  Long idInquilino,
  Long idPropiedad
) {
}
