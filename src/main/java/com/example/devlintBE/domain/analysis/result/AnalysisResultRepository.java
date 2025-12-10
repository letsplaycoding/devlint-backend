package com.example.devlintBE.domain.analysis.result;

import com.example.devlintBE.domain.analysis.request.AnalysisRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult> findByRequest(AnalysisRequest request);
}
