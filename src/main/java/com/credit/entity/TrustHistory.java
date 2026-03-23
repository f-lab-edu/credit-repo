package com.credit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trust_histories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustHistory {

    @Id
    @Column(name = "history_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private int scoreChange;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    public static TrustHistory create(Member member, String reason, int scoreChange) {
        return TrustHistory.builder()
                .id(UUID.randomUUID().toString())
                .member(member)
                .reason(reason)
                .scoreChange(scoreChange)
                .recordedAt(LocalDateTime.now())
                .build();
    }
}