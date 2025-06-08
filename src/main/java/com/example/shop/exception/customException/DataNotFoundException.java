package com.example.shop.exception.customException;

public class DataNotFoundException extends RuntimeException {
  public DataNotFoundException(String message) {
    super(message);
  }
}
