package com.example.app.daos;

import org.sql2o.Sql2o;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CrudDAO<T, ID> {

  public abstract Class<T> getTClass();

  public abstract String getTablePK();

  public abstract String getTableName();

  protected Sql2o sql2o() {
    return Sql2oDAO.getSql2o();
  }

  private static final Logger logger = LoggerFactory.getLogger(CrudDAO.class);

  protected Map<String, String> getColumnMappings() {
    return Map.of();
  }

  protected String resolveColumn(String fieldName) {
    String col = getColumnMappings().get(fieldName);
    return (col != null) ? col : fieldName;
  }

  public T insert(T t) {
    String pkField = getTablePK();

    Map<String, Object> params = extractNonNullParams(t);

    Object pkValue = params.get(pkField);
    if (pkValue == null) {
      params.remove(pkField);
    }

    if (params.isEmpty()) {
      throw new IllegalArgumentException("No hay campos para insertar.");
    }

    String columns = params.keySet().stream()
        .map(this::resolveColumn)
        .collect(Collectors.joining(", "));

    String values = params.keySet().stream()
        .map(c -> ":" + c)
        .collect(Collectors.joining(", "));

    String sql = "INSERT INTO " + getTableName() +
        " (" + columns + ") VALUES (" + values + ")";

    try (Connection con = sql2o().open()) {
      Query q = con.createQuery(sql, true);
      params.forEach(q::addParameter);

      var result = q.executeUpdate();

      if (pkValue == null) {
        Object generatedId = result.getKey();
        try {
          Field f = getTClass().getDeclaredField(pkField);
          f.setAccessible(true);
          f.set(t, ((Number) generatedId).longValue());
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new RuntimeException("No se pudo setear la PK generada", e);
        }
      }

      return t;
    } catch (Exception e) {
      throw new RuntimeException("Error al insertar en " + getTableName(), e);
    }
  }

  public Optional<T> findById(ID id) {
    String pkField = getTablePK();
    String pkColumn = resolveColumn(pkField);

    String sql = "SELECT * FROM " + getTableName() + " WHERE " + pkColumn + " = :id LIMIT 1";

    try (Connection con = sql2o().open()) {
      T row = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(getTClass());
      return Optional.ofNullable(row);
    }
  }

  public Optional<T> findOneBy(String column, Object value) {
    String col = resolveColumn(column);

    String sql = "SELECT * FROM " + getTableName() + " WHERE " + col + " = :v LIMIT 1";
    try (Connection con = sql2o().open()) {
      T row = con.createQuery(sql)
          .addParameter("v", value)
          .executeAndFetchFirst(getTClass());
      return Optional.ofNullable(row);
    }
  }

  public List<T> findAll() {
    String sql = "SELECT * FROM " + getTableName();
    try (Connection con = sql2o().open()) {
      return con.createQuery(sql).executeAndFetch(getTClass());
    }
  }

  public int update(T t) {
    String pkField = getTablePK();
    Object pkValue = getFieldValue(t, pkField);

    System.out.println(pkField);

    Map<String, Object> params = extractNonNullParams(t);
    params.remove(pkField);

    if (params.isEmpty())
      return 0;

    String setSql = params.keySet().stream()
        .map(c -> resolveColumn(c) + " = :" + c)
        .collect(Collectors.joining(", "));

    String pkColumn = resolveColumn(pkField);

    String sql = "UPDATE " + getTableName() +
        " SET " + setSql +
        " WHERE " + pkColumn + " = :_id";

    try (Connection con = sql2o().open()) {
      Query q = con.createQuery(sql);
      params.forEach(q::addParameter);
      q.addParameter("_id", pkValue);
      return q.executeUpdate().getResult();
    }
  }

  public int deleteById(ID id) {
    String pkField = getTablePK();
    String pkColumn = resolveColumn(pkField);

    String sql = "DELETE FROM " + getTableName() +
        " WHERE " + pkColumn + " = :id";
    try (Connection con = sql2o().open()) {
      return con.createQuery(sql)
          .addParameter("id", id)
          .executeUpdate()
          .getResult();
    }
  }

  private Map<String, Object> extractNonNullParams(T t) {
    Map<String, Object> map = new LinkedHashMap<>();
    for (Field f : getTClass().getDeclaredFields()) {
      if (Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers()))
        continue;
      f.setAccessible(true);
      String fieldName = f.getName();
      try {
        Object v = f.get(t);
        if (v != null)
          map.put(fieldName, v);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return map;
  }

  private Object getFieldValue(T t, String fieldName) {
    try {
      Field f = getTClass().getDeclaredField(fieldName);
      f.setAccessible(true);
      return f.get(t);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      for (Field f : getTClass().getDeclaredFields()) {
        if (f.getName().equalsIgnoreCase(fieldName)) {
          f.setAccessible(true);
          try {
            return f.get(t);
          } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
      return null;
    }
  }
}