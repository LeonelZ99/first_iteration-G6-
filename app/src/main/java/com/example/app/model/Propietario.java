package com.example.app.model;

// import jakarta.persistence.*;
import lombok.*;

// @Entity
@Setter
@Getter
@NoArgsConstructor
// @Table(name = "propietarios")
public class Propietario {
  // @Id
  // @Column(name = "id_propietario")
  private Long id;

  // @Column(name = "cbu", nullable = false, length = 22)
  private String cbu;

  // @OneToOne(fetch = FetchType.EAGER, optional = false)
  // @MapsId
  // @JoinColumn(name = "id_propietario", foreignKey = @ForeignKey(name = "fk_propietario_cliente"))
  private Cliente cliente;
}
