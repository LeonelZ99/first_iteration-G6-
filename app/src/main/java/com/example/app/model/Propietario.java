package com.example.app.model;

// import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class Propietario {
  private String cbu;
  private Cliente cliente;
}