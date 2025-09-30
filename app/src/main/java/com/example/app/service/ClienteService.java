package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.daos.ClienteDAO;
import com.example.app.model.Cliente;

import java.util.Optional;

@Service
public class ClienteService {
  private ClienteDAO clienteDao;

  public ClienteService(ClienteDAO clienteDao) {
    this.clienteDao = clienteDao;
  }
  
  public Optional<Cliente> getClienteById(Long idCliente) {
    return this.clienteDao.getClienteById(idCliente);
  }
}
