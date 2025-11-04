package com.example.app.dto;

import java.time.LocalDate;

public record ClienteDto(
  Long id,
  String nombre,
  String apellido,
  String direccion,
  LocalDate fechaNacimiento,
  String telefono,
  String mail,
  String cuil,
  String dni
) {}
