package com.example.app.service;

import com.example.app.daos.IPagoDAO;
import com.example.app.daos.IContratoDAO;
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
    private final IContratoDAO contratoDAO;

    public PagoService(IPagoDAO pagoDAO, IContratoDAO contratoDAO) {
        this.pagoDAO = pagoDAO;
        this.contratoDAO = contratoDAO;
    }

    public ReciboPagoDto generarPago(GenerarPagoRequest req) {
        if (req == null || req.contratoId() == null || req.periodo() == null) {
            throw new RuntimeException("Datos de pago inválidos");
        }

        // 1) Obtener contrato
        Contrato contrato = contratoDAO.getContratoById(req.contratoId())
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        if (!"VIGENTE".equalsIgnoreCase(contrato.getEstado())) {
            throw new RuntimeException("El contrato no está vigente");
        }

        // 2) Validar periodo dentro de la vigencia del contrato
        YearMonth periodo = parsePeriodo(req.periodo());
        YearMonth inicio = YearMonth.from(contrato.getFechaInicio());
        YearMonth fin = YearMonth.from(contrato.getFechaFin());
        if (periodo.isBefore(inicio) || periodo.isAfter(fin)) {
            throw new RuntimeException("Periodo fuera de la vigencia del contrato");
        }

        // 3) Chequear duplicado
        if (pagoDAO.existsPago(req.contratoId(), periodo.toString())) {
            throw new RuntimeException("El periodo ya está cancelado");
        }

        // 4) Calcular monto
        BigDecimal expensas = req.expensas() != null ? req.expensas() : BigDecimal.ZERO;
        BigDecimal impuestos = req.impuestos() != null ? req.impuestos() : BigDecimal.ZERO;
        BigDecimal precio = contrato.getPrecioMensual() != null ? contrato.getPrecioMensual() : BigDecimal.ZERO;
        BigDecimal monto = precio.add(expensas).add(impuestos);

        // 5) Fecha pago
        LocalDate fechaPago = req.fechaPago() != null ? req.fechaPago() : LocalDate.now();

        // 6) Insert
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
