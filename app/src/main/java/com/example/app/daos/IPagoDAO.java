package com.example.app.daos;

import com.example.app.dto.PagoDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IPagoDAO {
    boolean existsPago(Long contratoId, String periodo);

    Long insertPago(Long contratoId, String periodo, LocalDate fechaPago, String tipoPago,
            BigDecimal expensas, BigDecimal impuestos, BigDecimal monto);

    List<PagoDto> findPagosByContrato(Long contratoId);
}
