package com.example.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Respuesta/recibo del pago generado. */
public record ReciboPagoDto(
        Long reciboId,
        Long contratoId,
        String periodo,
        BigDecimal monto,
        LocalDate fechaEmision,
        String tipoPago,
        BigDecimal expensas,
        BigDecimal impuestos) {
}