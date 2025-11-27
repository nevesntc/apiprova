package br.com.neves.vassourasecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse response = new ErrorResponse("Invalid input", fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorMessage msg = new ErrorMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        ErrorMessage msg = new ErrorMessage("Erro interno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }

    static class ErrorResponse {
        private String error; private List<FieldErrorResponse> fields;
        public ErrorResponse(String error, List<FieldErrorResponse> fields) { this.error = error; this.fields = fields; }
        public String getError() { return error; }
        public List<FieldErrorResponse> getFields() { return fields; }
    }
    static class FieldErrorResponse {
        private String name; private String message;
        public FieldErrorResponse(String name, String message) { this.name = name; this.message = message; }
        public String getName() { return name; }
        public String getMessage() { return message; }
    }
    static class ErrorMessage {
        private String error;
        public ErrorMessage(String error) { this.error = error; }
        public String getError() { return error; }
    }
}
