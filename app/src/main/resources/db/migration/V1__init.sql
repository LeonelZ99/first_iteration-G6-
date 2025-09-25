-- Recomendado: crear base si no existe
CREATE DATABASE IF NOT EXISTS ayds
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ayds;

CREATE TABLE IF NOT EXISTS cliente (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre       VARCHAR(100) NOT NULL,
  direccion    VARCHAR(200),
  dni          VARCHAR(10)  UNIQUE,
  cuil         VARCHAR(15)  UNIQUE,
  rol          VARCHAR(50),
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS propiedad (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  precio        DECIMAL(12,2) NOT NULL DEFAULT 0,
  moneda        ENUM('ARS','USD') NOT NULL DEFAULT 'ARS',
  tipo          ENUM('CASA','DEPARTAMENTO','LOCAL','OTRO') NOT NULL DEFAULT 'OTRO',
  propietarioId BIGINT NOT NULL,
  direccion     VARCHAR(200),

  CONSTRAINT fk_propiedad_propietario
    FOREIGN KEY (propietarioId) REFERENCES cliente(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_propiedad_propietario ON propiedad(propietarioId);

CREATE TABLE IF NOT EXISTS contrato (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  propiedadId      BIGINT NOT NULL,
  garanteId        BIGINT NULL,
  inquilinoId      BIGINT NOT NULL,
  propietarioId    BIGINT NOT NULL,
  fechaInicio      DATE   NOT NULL,
  fechaFin         DATE   NULL,
  ultimaLiquidacion DATE  NULL,
  clausulas        TEXT   NULL,
  estado           ENUM('VIGENTE','FINALIZADO','SUSPENDIDO') NOT NULL DEFAULT 'VIGENTE',
  alquilerMensual  DECIMAL(12,2) NOT NULL DEFAULT 0,
  moneda           ENUM('ARS','USD') NOT NULL DEFAULT 'ARS',

  CONSTRAINT fk_contrato_propiedad
    FOREIGN KEY (propiedadId)   REFERENCES propiedad(id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_contrato_garante
    FOREIGN KEY (garanteId)     REFERENCES cliente(id)
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_contrato_inquilino
    FOREIGN KEY (inquilinoId)   REFERENCES cliente(id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_contrato_propietario
    FOREIGN KEY (propietarioId) REFERENCES cliente(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_contrato_propiedad   ON contrato(propiedadId);
CREATE INDEX idx_contrato_inquilino   ON contrato(inquilinoId);
CREATE INDEX idx_contrato_propietario ON contrato(propietarioId);
CREATE INDEX idx_contrato_garante     ON contrato(garanteId);

CREATE TABLE IF NOT EXISTS contrato_persona (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  contratoId  BIGINT NOT NULL,
  clienteId   BIGINT NOT NULL,
  rol         VARCHAR(50) NULL,

  CONSTRAINT uq_contrato_persona UNIQUE (contratoId, clienteId, rol),

  CONSTRAINT fk_cp_contrato FOREIGN KEY (contratoId) REFERENCES contrato(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_cp_cliente  FOREIGN KEY (clienteId)  REFERENCES cliente(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_cp_contrato ON contrato_persona(contratoId);
CREATE INDEX idx_cp_cliente  ON contrato_persona(clienteId);

-- =========================
-- 5) PAGO
-- =========================
CREATE TABLE IF NOT EXISTS pago (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  contratoId         BIGINT NOT NULL,
  medioPago          ENUM('EFECTIVO','TRANSFERENCIA','DEBITO','CREDITO','MERCADO_PAGO') NOT NULL DEFAULT 'EFECTIVO',
  importeAlquiler    DECIMAL(12,2) NOT NULL DEFAULT 0,
  tasaMunicipal      DECIMAL(12,2) NOT NULL DEFAULT 0,
  expensas           DECIMAL(12,2) NOT NULL DEFAULT 0,
  deposito           DECIMAL(12,2) NOT NULL DEFAULT 0,
  fechaEmision       DATE NOT NULL,
  fechaPago          DATE NULL,
  mesCorrespondiente DATE NOT NULL,  
  numeroRecibo       VARCHAR(50) UNIQUE,

  CONSTRAINT fk_pago_contrato FOREIGN KEY (contratoId) REFERENCES contrato(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_pago_contrato ON pago(contratoId);
CREATE INDEX idx_pago_mes      ON pago(mesCorrespondiente);
