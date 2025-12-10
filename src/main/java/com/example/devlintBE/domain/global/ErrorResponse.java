package com.example.devlintBE.domain.global;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ErrorResponse {

    // 에러 코드 (예: VALIDATION_ERROR, BAD_REQUEST, INTERNAL_SERVER_ERROR ...)
    private String code;

    // 사람이 읽을 수 있는 에러 메시지
    private String message;

    // 필드별 에러 메시지 (필요 없으면 null)
    private Map<String, String> validation;
}
