package com.example.app.controller;

import com.example.app.dto.BalanceDto;
import com.example.app.model.Caja;
import com.example.app.service.CajaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/caja")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    // GET /api/caja/movimientos?desde=2025-11-01&hasta=2025-11-30&tipo=INGRESO
    @GetMapping("/movimientos")
    public List<Caja> movimientos(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(required = false) String tipo) {
        return cajaService.obtenerMovimientos(desde, hasta, tipo);
    }

    // GET /api/caja/balance?mes=11&anio=2025
    @GetMapping("/balance")
    public BalanceDto balance(
            @RequestParam int mes,
            @RequestParam int anio) {
        return cajaService.consultarBalanceMensual(mes, anio);
    }
}
