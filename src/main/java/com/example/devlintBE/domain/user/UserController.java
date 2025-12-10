package com.example.devlintBE.domain.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 개발용: 테스트 유저 한명 만들어서 돌려주는 엔드포인트
    @PostMapping("/test-init")
    public ResponseEntity<User> createTestUser() {
        User user = userService.createTestUserIfNotExists();
        return ResponseEntity.ok(user);
    }
}
