package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContratoResponseDto(
                Long id,
                LocalDate fechaInicio,
                LocalDate fechaFin,
                String clausulas,
                String estado,
                BigDecimal montoMensual,
                BigDecimal depositoInicial,
                InquilinoDto inquilino,
                PropiedadDto propiedad,
                List<GaranteDto> garantes) {
}
