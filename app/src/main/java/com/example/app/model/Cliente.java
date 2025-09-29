package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "ingresos", scale = 2)
    private BigDecimal ingresos;

    @Column(name = "estado_civil", length = 45)
    private String estadoCivil;
    
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "mail", length = 80)
    private String mail;

    @Column(name = "dni", unique = true, length = 8)
    private String dni;

    @Column(name = "cuil", unique = true, length = 13)
    private String cuil;

    @OneToOne(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Propietario propietario;

    @OneToMany(mappedBy = "inquilino", fetch = FetchType.LAZY)
    private List<Contrato> contratos;

    @OneToMany(mappedBy = "propietario", fetch = FetchType.LAZY)
    private List<Propiedad> propiedades;
}
