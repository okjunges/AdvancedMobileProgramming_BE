package com.advancedMobileProgramming.global.util.jtw;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 간단한 인메모리 JWT 블랙리스트.
 * key = raw token, value = 만료 epoch seconds
 * 서버 재시작 시 초기화됨 (운영에서는 Redis 등으로 교체 권장)
 */
@Component
public class InMemoryTokenBlacklist {

    private final Map<String, Long> store = new ConcurrentHashMap<>();

    /** 토큰을 만료시각까지 블랙리스트에 등록 */
    public void blacklist(String token, Instant expiresAt) {
        store.put(token, expiresAt.getEpochSecond());
    }

    /** 블랙리스트 여부(만료된 항목은 조회 시 정리) */
    public boolean isBlacklisted(String token) {
        Long exp = store.get(token);
        if (exp == null) return false;
        long now = Instant.now().getEpochSecond();
        if (exp <= now) {
            store.remove(token);
            return false;
        }
        return true;
    }
}