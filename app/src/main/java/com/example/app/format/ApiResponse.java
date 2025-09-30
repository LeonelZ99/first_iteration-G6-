package com.example.app.format;

public record ApiResponse<T>(
  String status,
  String message,
  T data
) {
}