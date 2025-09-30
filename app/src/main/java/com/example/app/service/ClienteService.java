package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.daos.ClienteDAO;
import com.example.app.model.Cliente;

@Service
public class ClienteService {
  private final ClienteDAO clienteDao;

  public ClienteService(ClienteDAO clienteDao) {
    this.clienteDao = clienteDao;
  }
  
  public Cliente getClienteById(Long idCliente) {
    Cliente cliente = this.clienteDao.getClienteById(idCliente).orElse(null);
    
    if(cliente == null) {
      throw new RuntimeException("El cliente no existe");
    }

    return cliente;
  }
}
