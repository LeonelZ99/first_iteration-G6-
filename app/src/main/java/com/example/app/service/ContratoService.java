package com.example.app.service;

import com.example.app.model.Cliente;
import com.example.app.model.Propiedad;
import com.example.app.model.Contrato;

import com.example.app.daos.ContratoDAOO;

import org.springframework.stereotype.Service;

import java.lang.RuntimeException;

@Service 
public class ContratoService {
  private final ContratoDAOO contratoDao;
  private final ClienteService clienteService;
  private final PropiedadService propiedadService;

  public ContratoService(ContratoDAOO contratoDao, ClienteService clienteService, PropiedadService propiedadService) {
    this.contratoDao = contratoDao;
    this.clienteService = clienteService;
    this.propiedadService = propiedadService;
  }

  public Contrato saveContrato(Contrato contrato, Long idInquilino, Long idPropiedad) {
    Cliente inquilino = this.clienteService.getClienteById(idInquilino);
    
    if(inquilino == null) {
      throw new RuntimeException("No existe el inquilino");
    }
    
    Propiedad propiedad = this.propiedadService.getPropiedadById(idPropiedad);
    
    if(propiedad == null) {
      throw new RuntimeException("No existe la propiedad");
    }

    if(propiedad.getIdPropietario().equals(inquilino.getId())) {
      throw new RuntimeException("El inquilino es propietario de la propiedad seleccionada");
    }

    contrato.setIdInquilino(idInquilino);
    contrato.setIdPropiedad(idPropiedad);
    
    try {
      contratoDao.save(contrato);

      return contrato; 
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
