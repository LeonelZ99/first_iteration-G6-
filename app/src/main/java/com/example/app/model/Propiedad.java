package com.example.app.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "propiedades")
public class Propiedad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "tipo_pago", nullable = false, length = 45)
  private String direccion;

  @Column(name = "precio", scale = 2)
  private BigDecimal precio;

  @Column(name = "moneda", nullable = false)
  private String moneda;
  
  @Column(name = "tipo_propiedad", nullable = false)
  private String tipoPropiedad;

  @OneToMany(mappedBy = "propiedad", fetch = FetchType.LAZY)
  private List<Contrato> contratos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_propietario", referencedColumnName = "id")
  private Cliente propietario;
}
