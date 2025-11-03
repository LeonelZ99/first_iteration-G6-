package com.example.app.dto;

import java.math.BigDecimal;

public record BalanceDto(
        BigDecimal totalIngresos,
        BigDecimal totalEgresos,
        BigDecimal balance,
        int mes,
        int anio) {
}
