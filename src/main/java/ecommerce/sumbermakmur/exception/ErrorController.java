package ecommerce.sumbermakmur.exception;

import ecommerce.sumbermakmur.dto.response.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> handleResponseStatusException(ResponseStatusException e){
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.valueOf(e.getStatusCode().value()).getReasonPhrase())
                .message(e.getReason())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<WebResponse<String>> handleConstraintViolationException(ConstraintViolationException e){

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<WebResponse<String>> handleAccessDenied(AccessDeniedException e){
        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<WebResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        HttpStatus httpStatus = null;
        String status = null;
        String message = e.getMostSpecificCause().getMessage();
        String reason = null;

        if (message.contains("foreign key constraint")) {
            httpStatus = HttpStatus.BAD_REQUEST;
            status = HttpStatus.BAD_REQUEST.getReasonPhrase();
            reason = "cannot remove data because the data is used as reference by other table";
        } else if (message.contains("unique constraint") || message.contains("Duplicate entry")) {
            httpStatus = HttpStatus.CONFLICT;
            status = HttpStatus.CONFLICT.getReasonPhrase();
            reason = "Data duplicate";
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            status = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
            reason = "Internal Server Error";
        }

        WebResponse<String> response = WebResponse.<String>builder()
                .status(status)
                .message(reason)
                .build();
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

}
