package com.example.devlintBE.domain.payment;

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
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentType type; // ONE_TIME, SUBSCRIPTION

    private int amount;

    private LocalDateTime paidAt;

    public enum PaymentType {
        ONE_TIME,
        SUBSCRIPTION
    }
}