package com.example.devlintBE.domain.analysis.result;

import com.example.devlintBE.domain.analysis.request.AnalysisRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "analysis_result")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 요청의 결과인지 1:1 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_request_id")
    private AnalysisRequest request;

    // AI가 분석한 결과 (JSON 형태)
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String resultJson;

    // PDF URL 또는 파일 경로
    @Column(name = "report_url")
    private String reportUrl;
}
