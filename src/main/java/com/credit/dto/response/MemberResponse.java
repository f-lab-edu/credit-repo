package com.credit.dto.response;

import com.credit.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponse {
    private String id;
    private String name;
    private String email;
    private int trustScore;
    private LocalDateTime createdAt;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .trustScore(member.getTrustScore())
                .createdAt(member.getCreatedAt())
                .build();
    }
}