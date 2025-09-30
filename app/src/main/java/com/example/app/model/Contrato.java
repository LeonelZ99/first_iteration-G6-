package com.example.app.model;

// import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// @Entity
@Getter
@Setter
@NoArgsConstructor
// @Table(name = "contratos")
public class Contrato {
  // @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @Column(name = "id", nullable = false, unique = true)
  // private Long id;

  // @Column(name = "fecha_inicio", nullable = false)
  private LocalDate fechaInicio;

  // @Column(name = "fecha_fin")
  private LocalDate fechaFin;

  // @Column(name = "clausulas", nullable = false)
  private String clausulas;

  // @Column(name = "estado", nullable = false, length = 45)
  private String estado;

  // @Column(name = "monto_mensual", nullable = false, scale = 2)
  private BigDecimal montoMensual;

  // @Column(name = "deposito_inicial", nullable = false, length = 45, scale = 2)
  private BigDecimal depositoInicial;

  // @ManyToOne(fetch = FetchType.EAGER)
  // @JoinColumn(name = "id_inquilino", referencedColumnName = "id")
  private Cliente inquilino;

  // @ManyToOne(fetch = FetchType.EAGER)
  // @JoinColumn(name = "id_propiedad", referencedColumnName = "id")
  private Propiedad propiedad;

  // @OneToMany(mappedBy = "contrato", fetch = FetchType.EAGER)
  private List<Pago> pagos;
}
