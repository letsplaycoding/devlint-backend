package com.example.devlintBE.domain.analysis.dto;

import com.example.devlintBE.domain.analysis.request.AnalysisRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
// 한 번의 응답에 "요청 + 결과"를 같이 담아주는 DTO
public class AnalysisRequestResponseDto {

    private Long id;
    private Long userId;
    private String repoUrl;
    private AnalysisRequest.Status status;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;

    // 결과 부분
    private String resultJson;
    private String reportUrl;
}
