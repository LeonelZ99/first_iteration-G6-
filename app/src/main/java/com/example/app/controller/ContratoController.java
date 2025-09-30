package com.example.app.controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import com.example.app.format.ApiResponse;

import com.example.app.model.Contrato;
import com.example.app.service.ContratoService;

import com.example.app.dto.ContratoDto;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {
  private final ContratoService contratoService;
  
  public ContratoController(ContratoService contratoService) {
    this.contratoService = contratoService;
  }
  
  @GetMapping("/") 
  public ResponseEntity<ApiResponse<String>> HelloWorld() {
    ApiResponse<String> response = new ApiResponse<>(
      "success",
      "success message",
      "Hello world"
    );

    return ResponseEntity.ok(response);
  }

  @PostMapping("/")
  public ResponseEntity<ApiResponse<Contrato>> createContrato(
    @RequestBody Contrato contrato,
    @RequestParam(name = "inquilino") Long idInquilino,
    @RequestParam(name = "propiedad") Long idPropiedad
  ) {
    ApiResponse<Contrato> response = new ApiResponse<>(
      "ok",
      "contrato creado exitosamente",
      this.contratoService.saveContrato(contrato, idInquilino, idPropiedad)
    );

    return ResponseEntity.ok(response);
  }
}
