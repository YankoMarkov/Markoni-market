package org.yanmark.markoni.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Product already exist!")
public class ProductNameExistException extends RuntimeException {

    private int statusCode;

    public ProductNameExistException() {
        this.statusCode = 409;
    }

    public ProductNameExistException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
