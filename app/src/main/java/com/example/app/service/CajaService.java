package com.example.app.service;

import com.example.app.daos.ICajaDAO;
import com.example.app.model.Caja;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CajaService {

    private final ICajaDAO cajaDAO;
    private static final Logger logger = LoggerFactory.getLogger(CajaService.class);

    public CajaService(ICajaDAO cajaDAO) {
        this.cajaDAO = cajaDAO;
    }

    public List<Caja> obtenerTodos() {
        logger.info("Consultando todos los movimientos de caja");
        return cajaDAO.obtenerTodos();
    }
}
