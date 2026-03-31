package com.richardpdev.inventoryerp.common;

import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of(
                "error", "bad_request",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return Map.of(
                "error", "conflict",
                "message", "Resource was modified concurrently; retry the operation"
        );
    }
}