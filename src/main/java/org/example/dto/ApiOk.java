package org.example.dto;

public class ApiOk {
    public String code;
    public String message;

    public ApiOk(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
