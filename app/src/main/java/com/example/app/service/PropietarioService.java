package com.example.app.service;

import com.example.app.daos.PropietarioDAO;
import com.example.app.model.Propietario;

public class PropietarioService {
  private final PropietarioDAO propietarioDAO;

  public PropietarioService(PropietarioDAO propietarioDAO) {
    this.propietarioDAO = propietarioDAO;
  }
  
  public Propietario getPropietarioById(Long idCliente) {
    Propietario propietario = this.propietarioDAO.getPropietarioById(idCliente).orElse(null);
  
    if(propietario == null) {
      throw new RuntimeException("El propietario no existe");
    }

    return propietario;
  }
}
