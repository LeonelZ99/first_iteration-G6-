package com.example.app.daos;

import com.example.app.model.Contrato;

import java.util.Optional;

public interface IContratoDAO { 
  public void save(Contrato contrato);
  public Optional<Contrato> getContratoById(Long idContrato);
}
