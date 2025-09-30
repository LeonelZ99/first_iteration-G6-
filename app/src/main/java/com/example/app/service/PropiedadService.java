package com.example.app.service;

import com.example.app.model.*;
import com.example.app.daos.ClienteDAO;
import com.example.app.daos.PropiedadDAO;

import org.springframework.stereotype.Service;


@Service
public class PropiedadService {
  private final PropiedadDAO propiedadDao;

  public PropiedadService(PropiedadDAO propiedadDao) {
    this.propiedadDao = propiedadDao;
  }
  
  public Propiedad getPropiedadById(Long idPropiedad) {
    Propiedad propiedad = this.propiedadDao.getPropiedadById(idPropiedad).orElse(null);

    if(propiedad == null) {
      throw new RuntimeException("La propiedad no existe");
    }

    return propiedad;
  }
}
