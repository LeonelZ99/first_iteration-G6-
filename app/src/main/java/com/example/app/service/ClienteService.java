package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.daos.*;
import com.example.app.model.*;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteService {
  private final ClienteDAO clienteDao;

  private final GaranteDAO garanteDao;

  public ClienteService(ClienteDAO clienteDao, GaranteDAO garanteDao) {
    this.clienteDao = clienteDao;
    this.garanteDao = garanteDao;
  }

  public Cliente getCliente(Long idCliente) {
    Cliente cliente = this.clienteDao.getClienteById(idCliente).orElse(null);

    if (cliente == null) {
      throw new RuntimeException("El cliente no existe");
    }

    return cliente;
  }

  public Garante getGarante(Long idCliente) {
    Garante garante = this.garanteDao.getGaranteById(idCliente).orElse(null);

    if (garante == null) {
      throw new RuntimeException("El garante no existe");
    }

    return garante;
  }

  public void updateGarante(Garante garante) {
    this.garanteDao.update(garante);
  }

  public List<Long> getGarantesByContrato(Long idContrato) {
    return this.garanteDao.getGarantesByIdContrato(idContrato).stream()
        .map(Garante::getIdCliente)
        .filter(Objects::nonNull)
        .toList();
  }
}
