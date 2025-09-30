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
      "Hello world",
      null
    );

    return ResponseEntity.ok(response);
  }

  @PostMapping("/")
  public ResponseEntity<ApiResponse<Contrato>> createContrato(
    @RequestBody ContratoDto req
  ) {
    Contrato contrato = req.contrato();
    Long idInquilino = req.idInquilino();
    Long idPropiedad = req.idPropiedad();

    ApiResponse<Contrato> response = new ApiResponse<>(
      "ok",
      "contrato creado exitosamente",
      this.contratoService.saveContrato(contrato, idInquilino, idPropiedad),
      null
    );

    return ResponseEntity.ok(response);
  }
}
