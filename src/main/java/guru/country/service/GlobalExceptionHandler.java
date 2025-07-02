package guru.country.service;

import guru.country.controller.error.ApiError;
import guru.country.ex.NoSuchCountryByCodeException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${api.version}")
    private String apiVersion;

    @ExceptionHandler(NoSuchCountryByCodeException.class)
    public ResponseEntity<ApiError> handleCountryNotFoundException(NoSuchCountryByCodeException exception, HttpServletRequest request) {
        log.error(request.getRequestURI(), exception);
        return new ResponseEntity<>(
                new ApiError(
                        apiVersion,
                        HttpStatus.NOT_FOUND.toString(),
                        "Country not found",
                        request.getRequestURI(),
                        exception.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );

    }
}
