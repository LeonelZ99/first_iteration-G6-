package com.example.app.daos;

import com.example.app.dto.BalanceDto;
import com.example.app.model.Caja;

import java.time.LocalDate;
import java.util.List;

public interface ICajaDAO {

    List<Caja> obtenerPorPeriodo(LocalDate desde, LocalDate hasta, String tipoMovimiento);

    BalanceDto calcularBalance(LocalDate desde, LocalDate hasta, int mes, int anio);
}
