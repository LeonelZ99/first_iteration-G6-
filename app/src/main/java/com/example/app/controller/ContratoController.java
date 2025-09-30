package com.example.app.controller;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.example.app.format.ApiResponse;
import com.example.app.model.Cliente;
import com.example.app.model.Contrato;
import com.example.app.model.Propiedad;
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
    @RequestBody ContratoDto req
  ) {
    Contrato contrato = new Contrato();
    contrato.setFechaInicio(req.fechaInicio());
    contrato.setFechaFin(req.fechaFin());
    contrato.setClausulas(req.clausulas());
    contrato.setEstado(req.estado());
    contrato.setMontoMensual(req.montoMensual());
    contrato.setMontoMensual(req.depositoInicial());
    contrato.setInquilino(req.inquilino());
    contrato.setPropiedad(req.propiedad());

    ApiResponse<Contrato> response = new ApiResponse<>(
      "ok",
      "contrato creado exitosamente",
      this.contratoService.saveContrato(contrato, req.idInquilino(), req.idPropiedad())
    );

    return ResponseEntity.ok(response);
  }
}
