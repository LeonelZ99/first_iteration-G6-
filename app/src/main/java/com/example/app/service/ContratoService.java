package com.example.app.service;

import com.example.app.model.*;

import com.example.app.daos.*;

import org.springframework.stereotype.Service;

import java.lang.RuntimeException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.app.dto.*;

@Service 
public class ContratoService {
  private final ContratoDAOO contratoDao;
  private final GaranteDAO garanteDao;
  private final ClienteService clienteService;
  private final PropiedadService propiedadService;

  private static final Logger logger = LoggerFactory.getLogger(ContratoService.class);

  public ContratoService(ContratoDAOO contratoDao, ClienteService clienteService, PropiedadService propiedadService, GaranteDAO garanteDao) {
    this.contratoDao = contratoDao;
    this.clienteService = clienteService;
    this.propiedadService = propiedadService;
    this.garanteDao = garanteDao;
  }

  public GetContratoDto getContrato(Long idContrato) {
    Contrato contrato = this.contratoDao.getContratoById(idContrato).orElse(null);

    if (contrato == null) {
      throw new RuntimeException("El contrato no existe");
    };

    List<Long> garantes = this.clienteService.getGarantesByContrato(idContrato);

    return new GetContratoDto(
      contrato.getId(),
      contrato.getFechaInicio(),
      contrato.getFechaFin(),
      contrato.getClausulas(),
      contrato.getEstado(),
      contrato.getMontoMensual(),
      contrato.getDepositoInicial(),
      contrato.getIdInquilino(),
      contrato.getIdPropiedad(),
      garantes
    );
  }

  public Contrato saveContrato(ContratoDto contrato) {
    Cliente inquilino = this.clienteService.getCliente(contrato.idInquilino());
    Propiedad propiedad = this.propiedadService.getPropiedad(contrato.idPropiedad());
    // Cliente propietario = this.clienteService.getCliente(propiedad.getId());
    
    ArrayList<Garante> garantes = new ArrayList<>();

    for (GaranteDto garante : contrato.garantes()) {
      Garante garanteToAdd = this.clienteService.getGarante(garante.idGarante());
      
      garantes.add(garanteToAdd);
    }

    if(propiedad.getIdPropietario().equals(inquilino.getId())) {
      throw new RuntimeException("El inquilino es propietario de la propiedad seleccionada");
    }

    Contrato newContrato = new Contrato();
    newContrato.setFechaInicio(contrato.fechaInicio());
    newContrato.setFechaFin(contrato.fechaFin());
    newContrato.setClausulas(contrato.clausulas());
    newContrato.setEstado(contrato.estado());
    newContrato.setMontoMensual(contrato.montoMensual());
    newContrato.setDepositoInicial(contrato.depositoInicial());
    newContrato.setIdInquilino(contrato.idInquilino());
    newContrato.setIdPropiedad(contrato.idPropiedad());

    try {
      Contrato contratoCreated = this.contratoDao.insert(newContrato);
      
      for(Garante g: garantes) {
        System.out.println(g);
        g.setIdContrato(contratoCreated.getId());
        clienteService.updateGarante(g);
      }
 
      return newContrato; 
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
