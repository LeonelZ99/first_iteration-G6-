package com.example.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// import jakarta.persistence.*;
import lombok.*;

// @Entity
@Setter
@Getter
@NoArgsConstructor
// @Table(name = "pagos")
public class Pago {
  // @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @Column(name = "id", nullable = false, unique = true)
  private Long id;

  // @Column(name = "tipo_pago", nullable = false, length = 45)
  private String tipoPago;

  // @Column(name = "fecha_pago")
  private LocalDate fechaPago;

  // @Column(name = "monto", scale = 2)
  private BigDecimal monto;
  
  // @Column(name = "expensas", scale = 2)
  private BigDecimal expensas;

  // @ManyToOne(fetch = FetchType.LAZY)
  // @JoinColumn(name = "id_contrato", referencedColumnName = "id")
  private Contrato contrato;
}
