package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Caja {

    private Integer id;
    private LocalDate fecha;
    private String tipoMovimiento;
    private String concepto;
    private BigDecimal monto;
    private String metodoPago;
    private String categoria;
    private String observaciones;
}
