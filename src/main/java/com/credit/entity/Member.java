package com.credit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int trustScore;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Member create(String name, String email) {
        LocalDateTime now = LocalDateTime.now();
        return Member.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .email(email)
                .trustScore(100)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void deductTrustScore(int amount) {
        this.trustScore -= amount;
        this.updatedAt = LocalDateTime.now();
    }
}