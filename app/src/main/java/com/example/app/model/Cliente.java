package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Cliente {
    private Long id;
    private String nombre;
    private String apellido;
    private String direccion;
    private LocalDate fechaNacimiento;
    private BigDecimal ingresos;
    private String estadoCivil;
    private String telefono;
    private String mail;
    private String dni;
    private String cuil;
}
