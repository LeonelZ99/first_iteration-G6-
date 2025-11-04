package com.example.app.controller;

import org.springframework.web.bind.annotation.*;


import org.springframework.http.ResponseEntity;

import com.example.app.format.ApiResponse;
import com.example.app.model.Contrato;
import com.example.app.service.ContratoService;

import com.example.app.dto.*;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {
  private final ContratoService contratoService;
  
  public ContratoController(ContratoService contratoService) {
    this.contratoService = contratoService;
  }
  
  // @GetMapping("/") 
  // public ResponseEntity<ApiResponse<String>> HelloWorld() {
    // ApiResponse<String> response = new ApiResponse<>(
      // "success",
      // "success message",
      // "Hello world"/
    // );

    // return ResponseEntity.ok(response);
  // }

  @PostMapping("/")
  public ResponseEntity<ApiResponse<Object>> createContrato(
    @RequestBody ContratoDto contratoDto
  ) {
    ApiResponse<Object> response = new ApiResponse<Object>(
      "ok",
      "contrato creado exitosamente",
      this.contratoService.saveContrato(contratoDto)
    );

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GetContratoDto>> createContrato(
    @PathVariable("id") Long id
  ) {

    ApiResponse<GetContratoDto> response = new ApiResponse<>(
      "ok",
      "contrato creado exitosamente",
      this.contratoService.getContrato(id)
    );

    return ResponseEntity.ok(response);
  }
}
