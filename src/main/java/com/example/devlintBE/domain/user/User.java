package com.example.devlintBE.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;   // 로그인 이메일

    private String password; // 소셜 로그인 시 null 가능

    @Column(name = "github_username")
    private String githubUsername;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(nullable = false)
    private String role; // USER / ADMIN
}
