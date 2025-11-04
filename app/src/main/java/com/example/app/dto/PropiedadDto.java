package com.example.app.dto;

import java.math.BigDecimal;

public record PropiedadDto(
  Long id,
  String direccion,
  BigDecimal precio, 
  String moneda,
  String tipoPropiedad,
  PropietarioDto propietario
) {}
