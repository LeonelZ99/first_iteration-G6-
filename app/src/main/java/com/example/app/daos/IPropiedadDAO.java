package com.example.app.daos;

import com.example.app.model.Propiedad;
import java.util.Optional;

public interface IPropiedadDAO {
  public Optional<Propiedad> getPropiedadById(Long idPropiedad);
}
