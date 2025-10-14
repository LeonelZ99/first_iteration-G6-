package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Request para generar un pago de un periodo específico (YYYY-MM). */
public record GenerarPagoRequest(
        Long contratoId,
        LocalDate periodo,
        LocalDate fechaPago,
        String tipoPago,
        BigDecimal expensas,
        BigDecimal impuestos) {
}