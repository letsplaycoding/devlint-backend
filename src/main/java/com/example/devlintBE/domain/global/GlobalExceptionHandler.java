package com.example.devlintBE.domain.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) DTO 검증 실패 (ex. @Valid 에서 걸림)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> validation = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse body = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("요청 값이 올바르지 않습니다.")
                .validation(validation)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 2) 서비스에서 던진 IllegalArgumentException (ex. 유저 없음 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        ErrorResponse body = ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 3) 그 외 모든 예외 (예상 못 한 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        // 실제 서비스면 여기서 로그를 남기는 게 중요함 (ex.printStackTrace() 대신 Logger)

        ErrorResponse body = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("서버에서 알 수 없는 오류가 발생했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
