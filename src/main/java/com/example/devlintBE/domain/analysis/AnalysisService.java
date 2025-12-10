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
}
