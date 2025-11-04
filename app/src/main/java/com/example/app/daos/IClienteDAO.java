package com.example.app.daos;

import com.example.app.model.*;
import java.util.Optional;

public interface IClienteDAO {
  public Optional<Cliente> getClienteById(Long idCliente);
}
