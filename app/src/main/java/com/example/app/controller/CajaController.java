package com.example.app.controller;

import com.example.app.model.Caja;
import com.example.app.service.CajaService;
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
    public List<Caja> getAll() {
        return cajaService.obtenerTodos();
    }
}
