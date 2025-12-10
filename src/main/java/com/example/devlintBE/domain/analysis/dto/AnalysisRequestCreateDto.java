package com.example.devlintBE.domain.analysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnalysisRequestCreateDto {

    // 아직 인증 붙이기 전이니까 userId를 Body로 받는 형태로 시작
    @NotNull(message = "userId는 필수 값입니다.")
    private Long userId;

    // 분석할 GitHub repo URL
    @NotBlank(message = "repoUrl은 비워둘 수 없습니다.")
    private String repoUrl;
}
