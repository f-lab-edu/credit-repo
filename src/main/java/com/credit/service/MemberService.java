package com.credit.service;

import com.credit.dto.request.MemberCreateRequest;
import com.credit.dto.response.MemberResponse;
import com.credit.entity.Member;
import com.credit.exception.CustomException;
import com.credit.exception.ErrorType;
import com.credit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException(ErrorType.EMAIL_ALREADY_IN_USE);
        }

        Member member = Member.create(request.getName(), request.getEmail());
        return MemberResponse.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorType.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }
}