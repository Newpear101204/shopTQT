package com.example.shop.exception.customException;

public class AccountExist extends RuntimeException {
    public AccountExist(String message) {
        super(message);
    }
}
