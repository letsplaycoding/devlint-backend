package com.example.devlintBE.domain.analysis;

import com.example.devlintBE.domain.analysis.dto.AnalysisRequestCreateDto;
import com.example.devlintBE.domain.analysis.dto.AnalysisRequestResponseDto;
import com.example.devlintBE.domain.analysis.request.AnalysisRequest;
import com.example.devlintBE.domain.analysis.request.AnalysisRequestRepository;
import com.example.devlintBE.domain.analysis.result.AnalysisResult;
import com.example.devlintBE.domain.analysis.result.AnalysisResultRepository;
import com.example.devlintBE.domain.user.User;
import com.example.devlintBE.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AnalysisService {

    private final AnalysisRequestRepository analysisRequestRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final UserRepository userRepository;

    public AnalysisService(AnalysisRequestRepository analysisRequestRepository, AnalysisResultRepository analysisResultRepository, UserRepository userRepository) {
        this.analysisRequestRepository = analysisRequestRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.userRepository = userRepository;
    }

    // -------------------------------
    // 1) 분석 요청 생성
    // -------------------------------
    @Transactional
    public AnalysisRequestResponseDto createAnalysisRequest(AnalysisRequestCreateDto dto) {

        // 1-1) 유저 존재 여부 확인
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + dto.getUserId()));

        // 1-2) AnalysisRequest 엔터티 생성
        AnalysisRequest request = AnalysisRequest.builder()
                .user(user)
                .repoUrl(dto.getRepoUrl())
                .status(AnalysisRequest.Status.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        AnalysisRequest saved = analysisRequestRepository.save(request);

        // 1-3) 응답 DTO로 변환 (결과는 아직 없음)
        return AnalysisRequestResponseDto.builder()
                .id(saved.getId())
                .userId(user.getId())
                .repoUrl(saved.getRepoUrl())
                .status(saved.getStatus())
                .requestedAt(saved.getRequestedAt())
                .completedAt(saved.getCompletedAt())
                .resultJson(null)
                .reportUrl(null)
                .build();
    }

    // -------------------------------
    // 2) 분석 요청 + 결과 조회
    // -------------------------------
    @Transactional(readOnly = true)
    public AnalysisRequestResponseDto getAnalysisRequest(Long id) {

        AnalysisRequest request = analysisRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분석 요청입니다. id=" + id));

        // 결과가 있을 수도, 없을 수도 있으니 Optional 사용
        AnalysisResult result = analysisResultRepository.findByRequest(request).orElse(null);

        return AnalysisRequestResponseDto.builder()
                .id(request.getId())
                .userId(request.getUser().getId())
                .repoUrl(request.getRepoUrl())
                .status(request.getStatus())
                .requestedAt(request.getRequestedAt())
                .completedAt(request.getCompletedAt())
                .resultJson(result != null ? result.getResultJson() : null)
                .reportUrl(result != null ? result.getReportUrl() : null)
                .build();
    }

    @Transactional
    public AnalysisRequestResponseDto runAnalysis(Long requestId) {

        // 1) 요청 조회
        AnalysisRequest request = analysisRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분석 요청입니다. id=" + requestId));

        // 2) 상태 체크: 이미 처리 중이거나 끝난 요청이면 막기
        if (request.getStatus() != AnalysisRequest.Status.PENDING) {
            throw new IllegalStateException("PENDING 상태의 요청만 실행할 수 있습니다. 현재 상태=" + request.getStatus());
        }

        // 3) PROCESSING 상태로 변경 (분석 시작)
        request.setStatus(AnalysisRequest.Status.PROCESSING);
        analysisRequestRepository.save(request);

        try {
            // 4) 실제 분석 호출 (현재는 더미 함수로 구현)
            String resultJson = callAiAnalyzer(request.getRepoUrl());

            // 5) 결과 엔터티 생성 및 저장
            AnalysisResult result = AnalysisResult.builder()
                    .request(request)
                    .resultJson(resultJson)
                    .reportUrl(null)      // 나중에 PDF 생성하면 채우기
                    .build();

            analysisResultRepository.save(result);

            // 6) 요청 상태 SUCCESS로 변경 + 완료 시간 기록
            request.setStatus(AnalysisRequest.Status.SUCCESS);
            request.setCompletedAt(LocalDateTime.now());
            analysisRequestRepository.save(request);

        } catch (Exception e) {
            // 7) 에러 발생 시 FAILED로 마킹
            request.setStatus(AnalysisRequest.Status.FAILED);
            request.setCompletedAt(LocalDateTime.now());
            analysisRequestRepository.save(request);

            // GlobalExceptionHandler에서 처리하도록 그대로 던짐
            throw e;
        }

        // 8) 최종적으로 요청 + 결과를 담은 DTO 리턴
        return getAnalysisRequest(request.getId());
    }

    // -------------------------------
    // (임시) 실제 AI 분석 대신 쓰는 더미 함수
    // 나중에 여기를 HTTP 호출로 교체하면 됨
    // -------------------------------
    private String callAiAnalyzer(String repoUrl) {
        // 지금은 샘플 JSON 문자열만 돌려주자
        return """
        {
          "summary": "이 리포지토리는 DevLint 테스트용으로 분석되었습니다.",
          "repoUrl": "%s",
          "issues": [],
          "suggestions": [
            "테스트 단계에서는 이 더미 결과를 사용합니다.",
            "나중에 여기서 실제 AI 서버를 호출하도록 교체하세요."
          ]
        }
        """.formatted(repoUrl);
    }
}
