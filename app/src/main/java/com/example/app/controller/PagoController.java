package com.example.app.controller;

import com.example.app.dto.GenerarPagoRequest;
import com.example.app.dto.ReciboPagoDto;
import com.example.app.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<ReciboPagoDto> generar(@RequestBody GenerarPagoRequest request) {
        ReciboPagoDto recibo = pagoService.generarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(recibo);
    }
}
