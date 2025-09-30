package com.example.app.service;

import com.example.app.model.Cliente;
import com.example.app.model.Propiedad;
import com.example.app.model.Contrato;

import com.example.app.daos.ContratoDAOO;

import org.springframework.stereotype.Service;

import java.lang.RuntimeException;

@Service 
public class ContratoService {
  private ContratoDAOO contratoDao;
  private ClienteService clienteService;
  private PropiedadService propiedadService;
  // private PagoService pagoService;

  public ContratoService(ContratoDAOO contratoDao, ClienteService clienteService, PropiedadService propiedadService) {
    this.contratoDao = contratoDao;
    this.clienteService = clienteService;
    this.propiedadService = propiedadService;
    // this.pagoService = pagoService;
  }

  public Contrato saveContrato(Contrato contrato, Long idInquilino, Long idPropiedad) {
    Cliente inquilino = this.clienteService.getClienteById(idInquilino).orElse(null);
    
    // si inquilino es null throw error
    if(inquilino == null) {
      throw new RuntimeException("No existe el inquilino");
    }
    
    Propiedad propiedad = this.propiedadService.getPropiedadById(idPropiedad).orElse(null);
    
    // si propiedad es null throw error
    if(propiedad == null) {
      throw new RuntimeException("No existe la propiedad");
    }

    Contrato newContrato = new Contrato();

    newContrato.setFechaInicio(contrato.getFechaInicio());
    newContrato.setFechaFin(contrato.getFechaFin());
    newContrato.setClausulas(contrato.getClausulas());
    newContrato.setEstado(contrato.getEstado());
    newContrato.setMontoMensual(contrato.getMontoMensual());
    newContrato.setDepositoInicial(contrato.getDepositoInicial());
    newContrato.setInquilino(inquilino);
    newContrato.setPropiedad(propiedad);

    contratoDao.save(newContrato);

    return newContrato; 
  }
}
