package org.yanmark.markoni.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Category already exist!")
public class CategoryNameExistException extends RuntimeException {

    private int statusCode;

    public CategoryNameExistException() {
        this.statusCode = 409;
    }

    public CategoryNameExistException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
