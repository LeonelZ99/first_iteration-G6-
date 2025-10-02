package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoDto(
        Long id,
        Long contratoId,
        String periodo,
        LocalDate fechaPago,
        String tipoPago,
        BigDecimal expensas,
        BigDecimal impuestos,
        BigDecimal monto) {
}
