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
  private final InquilinoDAO inquilinoDao;
  private final PropietarioDAO propietarioDao;

  private static final Logger logger = LoggerFactory.getLogger(ContratoService.class);

  public ContratoService(ContratoDAOO contratoDao, ClienteService clienteService, PropiedadService propiedadService,
      GaranteDAO garanteDao, InquilinoDAO inquilinoDao, PropietarioDAO propietarioDao) {
    this.contratoDao = contratoDao;
    this.clienteService = clienteService;
    this.propiedadService = propiedadService;
    this.garanteDao = garanteDao;
    this.inquilinoDao = inquilinoDao;
    this.propietarioDao = propietarioDao;
  }

  public ContratoResponseDto getContrato(Long idContrato) {
    // 1) Traer contrato
    Contrato contrato = this.contratoDao.getContratoById(idContrato)
        .orElseThrow(() -> new RuntimeException("El contrato no existe"));

    // 2) Inquilino socioeconómico
    var inquilinoOpt = inquilinoDao.getByClienteId(contrato.getIdInquilino());
    if (inquilinoOpt.isEmpty()) {
      throw new RuntimeException("El inquilino no existe como inquilino (tabla inquilinos)");
    }
    Inquilino inquilino = inquilinoOpt.get();

    InquilinoDto inquilinoDto = new InquilinoDto(
        inquilino.getEstadoCivil(),
        inquilino.getIngresos(),
        inquilino.getCantPersonasConvive(),
        inquilino.getTrabajo(),
        inquilino.getClientesId());

    // 3) Propiedad + propietario
    Propiedad propiedad = propiedadService.getPropiedad(contrato.getIdPropiedad());

    var propietarioOpt = propietarioDao.getPropietarioById(propiedad.getIdPropietario());
    if (propietarioOpt.isEmpty()) {
      throw new RuntimeException("Propietario no encontrado");
    }
    Propietario propietario = propietarioOpt.get();

    Cliente propietarioCliente = clienteService.getCliente(propietario.getClientesId());
    ClienteDto propietarioClienteDto = new ClienteDto(
        propietarioCliente.getId(),
        propietarioCliente.getNombre(),
        propietarioCliente.getApellido(),
        propietarioCliente.getDireccion(),
        propietarioCliente.getFechaNacimiento(),
        propietarioCliente.getTelefono(),
        propietarioCliente.getMail(),
        propietarioCliente.getCuil(),
        propietarioCliente.getDni());

    PropietarioDto propietarioDto = new PropietarioDto(
        propietario.getCbu(),
        propietarioClienteDto);

    PropiedadDto propiedadDto = new PropiedadDto(
        propiedad.getId(),
        propiedad.getDireccion(),
        propiedad.getPrecio(),
        propiedad.getMoneda(),
        propiedad.getTipoPropiedad(),
        propietarioDto);

    // 4) Garantes: primero ids, después datos completos
    List<Long> idsGarantes = clienteService.getGarantesByContrato(contrato.getId());

    List<Garante> garantes = idsGarantes.stream()
        .map(id -> garanteDao.getGaranteById(id)
            .orElseThrow(() -> new RuntimeException("Garante no encontrado: " + id)))
        .toList();

    List<GaranteDto> garantesDto = garantes.stream()
        .map(g -> new GaranteDto(
            g.getIdCliente(), // id del cliente garante
            g.getIngresos(),
            g.getTrabajo(),
            g.getIdContrato()))
        .toList();

    // 5) Armar DTO de respuesta
    return new ContratoResponseDto(
        contrato.getId(),
        contrato.getFechaInicio(),
        contrato.getFechaFin(),
        contrato.getClausulas(),
        contrato.getEstado(),
        contrato.getMontoMensual(),
        contrato.getDepositoInicial(),
        inquilinoDto,
        propiedadDto,
        garantesDto);
  }

  public ContratoResponseDto saveContrato(ContratoDto dto) {

    // 1) Entidades básicas
    Cliente inquilinoCliente = clienteService.getCliente(dto.idInquilino());
    Propiedad propiedad = propiedadService.getPropiedad(dto.idPropiedad());

    // inquilino socioeconómico
    var inquilinoOpt = inquilinoDao.getByClienteId(dto.idInquilino());
    if (inquilinoOpt.isEmpty()) {
      throw new RuntimeException("El inquilino no existe como inquilino (tabla inquilinos)");
    }
    Inquilino inquilino = inquilinoOpt.get();

    // propietario de la propiedad
    var propietarioOpt = propietarioDao.getPropietarioById(propiedad.getIdPropietario());
    if (propietarioOpt.isEmpty()) {
      throw new RuntimeException("Propietario no encontrado");
    }
    Propietario propietario = propietarioOpt.get();

    // garantes
    List<Garante> garantes = dto.idGarantes().stream()
        .map(id -> garanteDao.getGaranteById(id)
            .orElseThrow(() -> new RuntimeException("Garante no encontrado: " + id)))
        .toList();

    // 2) Regla de negocio
    if (propiedad.getIdPropietario().equals(inquilinoCliente.getId())) {
      throw new RuntimeException("El inquilino es propietario de la propiedad seleccionada");
    }

    // 3) Crear contrato en BD
    Contrato contrato = new Contrato();
    contrato.setFechaInicio(dto.fechaInicio());
    contrato.setFechaFin(dto.fechaFin());
    contrato.setClausulas(dto.clausulas());
    contrato.setEstado(dto.estado());
    contrato.setMontoMensual(dto.montoMensual());
    contrato.setDepositoInicial(dto.depositoInicial());
    contrato.setIdInquilino(dto.idInquilino());
    contrato.setIdPropiedad(dto.idPropiedad());

    Contrato contratoCreated = contratoDao.insert(contrato);

    for (Garante g : garantes) {
      g.setIdContrato(contratoCreated.getId());
      clienteService.updateGarante(g);
    }

    // 4) Mapear ENTIDADES -> DTOs con datos completos

    // InquilinoDto
    InquilinoDto inquilinoDto = new InquilinoDto(
        inquilino.getEstadoCivil(),
        inquilino.getIngresos(),
        inquilino.getCantPersonasConvive(),
        inquilino.getTrabajo(),
        inquilino.getClientesId());

    Cliente propietarioCliente = clienteService.getCliente(propietario.getClientesId());
    ClienteDto propietarioClienteDto = new ClienteDto(
        propietarioCliente.getId(),
        propietarioCliente.getNombre(),
        propietarioCliente.getApellido(),
        propietarioCliente.getDireccion(),
        propietarioCliente.getFechaNacimiento(),
        propietarioCliente.getTelefono(),
        propietarioCliente.getMail(),
        propietarioCliente.getCuil(),
        propietarioCliente.getDni());

    PropietarioDto propietarioDto = new PropietarioDto(
        propietario.getCbu(),
        propietarioClienteDto);

    PropiedadDto propiedadDto = new PropiedadDto(
        propiedad.getId(),
        propiedad.getDireccion(),
        propiedad.getPrecio(),
        propiedad.getMoneda(),
        propiedad.getTipoPropiedad(),
        propietarioDto);

    // GarantesDto
    List<GaranteDto> garantesDto = garantes.stream()
        .map(g -> new GaranteDto(
            g.getIdCliente(), // idGarante = cliente garante
            g.getIngresos(),
            g.getTrabajo(),
            g.getIdContrato()))
        .toList();

    // 5) DTO de respuesta
    return new ContratoResponseDto(
        contratoCreated.getId(),
        contratoCreated.getFechaInicio(),
        contratoCreated.getFechaFin(),
        contratoCreated.getClausulas(),
        contratoCreated.getEstado(),
        contratoCreated.getMontoMensual(),
        contratoCreated.getDepositoInicial(),
        inquilinoDto,
        propiedadDto,
        garantesDto);
  }

}
