package com.example.app.controller;

import com.example.app.dto.ContratoResponseDto;
import com.example.app.format.ApiResponse;
import com.example.app.model.Caja;
import com.example.app.service.CajaService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/caja")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping("/movimientos")
    public ResponseEntity<ApiResponse<List<Caja>>> getAll() {
        ApiResponse<List<Caja>> response = new ApiResponse<>(
                "200",
                "movimientos obtenidos exitosamente",
                this.cajaService.obtenerTodos());

        return ResponseEntity.ok(response);
    }
}
