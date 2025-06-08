package com.example.shop.exception.customException;

public class DuplicatedUsername extends RuntimeException {
    public DuplicatedUsername(String message) {
        super(message);
    }
}
