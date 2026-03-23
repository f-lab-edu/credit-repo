package com.credit.repository;

import com.credit.entity.TrustHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrustHistoryRepository extends JpaRepository<TrustHistory, String> {
    List<TrustHistory> findByMemberIdOrderByRecordedAtDesc(String memberId);
}