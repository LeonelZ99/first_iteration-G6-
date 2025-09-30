package com.example.app.service;

import com.example.app.model.Propiedad;

import com.example.app.daos.PropiedadDAO;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropiedadService {
  private PropiedadDAO propiedadDao;

  public PropiedadService(PropiedadDAO propiedadDao) {
    this.propiedadDao = propiedadDao;
  }
  
  public Optional<Propiedad> getPropiedadById(Long idPropiedad) {
    return this.propiedadDao.getPropiedadById(idPropiedad);
  }
}
