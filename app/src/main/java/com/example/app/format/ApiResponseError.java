package com.example.app.format;

public record ApiResponseError<T>(
  String status,
  T data,
  Object metadata
) {
}