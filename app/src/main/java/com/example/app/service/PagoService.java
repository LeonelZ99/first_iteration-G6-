package com.example.app.service;

import com.example.app.daos.IPagoDAO;
import com.example.app.daos.ContratoDAOO;
import com.example.app.dto.GenerarPagoRequest;
import com.example.app.dto.ReciboPagoDto;
import com.example.app.model.Contrato;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class PagoService {

    private final IPagoDAO pagoDAO;
    private final ContratoDAOO contratoDAO;

    public PagoService(IPagoDAO pagoDAO, ContratoDAOO contratoDAO) {
        this.pagoDAO = pagoDAO;
        this.contratoDAO = contratoDAO;
    }

    public ReciboPagoDto generarPago(GenerarPagoRequest req) {
        if (req == null || req.contratoId() == null || req.periodo() == null) {
            throw new RuntimeException("Datos de pago inválidos");
        }

        Contrato contrato = contratoDAO.getContratoById(req.contratoId())
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        if (!"VIGENTE".equalsIgnoreCase(contrato.getEstado())) {
            throw new RuntimeException("El contrato no está vigente");
        }

        LocalDate periodo = req.periodo();

        BigDecimal expensas = req.expensas() != null ? req.expensas() : BigDecimal.ZERO;
        BigDecimal impuestos = req.impuestos() != null ? req.impuestos() : BigDecimal.ZERO;
        BigDecimal precio = contrato.getMontoMensual() != null ? contrato.getMontoMensual() : BigDecimal.ZERO;
        BigDecimal monto = precio.add(expensas).add(impuestos);

        LocalDate fechaPago = req.fechaPago() != null ? req.fechaPago() : LocalDate.now();

        Long reciboId = pagoDAO.insertPago(
                req.contratoId(),
                periodo.toString(),
                fechaPago,
                req.tipoPago(),
                expensas,
                impuestos,
                monto);

        // 7) Recibo
        return new ReciboPagoDto(
                reciboId,
                req.contratoId(),
                periodo.toString(),
                monto,
                LocalDate.now(),
                req.tipoPago(),
                expensas,
                impuestos);
    }

    private YearMonth parsePeriodo(String periodo) {
        try {
            return YearMonth.parse(periodo);
        } catch (Exception e) {
            throw new RuntimeException("Formato de periodo inválido. Use 'YYYY-MM'");
        }
    }
}
