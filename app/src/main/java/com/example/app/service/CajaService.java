package com.example.app.service;

import com.example.app.daos.ICajaDAO;
import com.example.app.dto.BalanceDto;
import com.example.app.model.Caja;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CajaService {

    private final ICajaDAO cajaDAO;

    public CajaService(ICajaDAO cajaDAO) {
        this.cajaDAO = cajaDAO;
    }

    public List<Caja> obtenerMovimientos(LocalDate desde, LocalDate hasta, String tipoMovimiento) {
        return cajaDAO.obtenerPorPeriodo(desde, hasta, tipoMovimiento);
    }

    public BalanceDto consultarBalanceMensual(int mes, int anio) {
        LocalDate desde = LocalDate.of(anio, mes, 1);
        LocalDate hasta = desde.withDayOfMonth(desde.lengthOfMonth());
        return cajaDAO.calcularBalance(desde, hasta, mes, anio);
    }
}
