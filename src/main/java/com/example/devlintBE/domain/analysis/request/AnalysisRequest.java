package com.example.devlintBE.domain.analysis.request;

import com.example.devlintBE.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "analysis_request")
public class AnalysisRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 유저가 요청했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 분석할 GitHub 저장소 URL
    @Column(nullable = false)
    private String repoUrl;

    // 분석 상태
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        PROCESSING,
        SUCCESS,
        FAILED
    }

    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;
}