package com.example.devlintBE.domain.user;

import com.example.devlintBE.domain.user.User;
import com.example.devlintBE.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 이미 있으면 그대로 돌려주고, 없으면 새로 만드는 테스트용 메서드
    @Transactional
    public User createTestUserIfNotExists() {

        return userRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    User user = User.builder()
                            .email("test@devlint.com")
                            .password("test1234")          // 나중에 암호화 예정
                            .githubUsername("testgithub")
                            .profileImage(null)
                            .role("USER")
                            .build();
                    return userRepository.save(user);
                });
    }
}
