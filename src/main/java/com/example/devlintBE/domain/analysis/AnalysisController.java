package com.example.devlintBE.domain.analysis;

import com.example.devlintBE.domain.analysis.dto.AnalysisRequestCreateDto;
import com.example.devlintBE.domain.analysis.dto.AnalysisRequestResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // -------------------------------
    // 1) 분석 요청 생성
    // POST /api/analysis/requests
    // -------------------------------
    @PostMapping("/requests")
    public ResponseEntity<AnalysisRequestResponseDto> createAnalysisRequest(
            @Valid @RequestBody AnalysisRequestCreateDto requestDto
    ) {
        AnalysisRequestResponseDto responseDto = analysisService.createAnalysisRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // -------------------------------
    // 2) 분석 요청 + 결과 조회
    // GET /api/analysis/requests/{id}
    // -------------------------------
    @GetMapping("/requests/{id}")
    public ResponseEntity<AnalysisRequestResponseDto> getAnalysisRequest(
            @PathVariable Long id
    ) {
        AnalysisRequestResponseDto responseDto = analysisService.getAnalysisRequest(id);
        return ResponseEntity.ok(responseDto);
    }

    // -------------------------------
    // 3) 분석 실행
    // POST /api/analysis/requests/{id}/run
    // -------------------------------
    @PostMapping("/requests/{id}/run")
    public ResponseEntity<AnalysisRequestResponseDto> runAnalysis(
            @PathVariable Long id
    ) {
        AnalysisRequestResponseDto responseDto = analysisService.runAnalysis(id);
        return ResponseEntity.ok(responseDto);
    }
}
