package com.example.demo.util;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class RateLimiterFactory {

    private RateLimiterConfig getRateLimiterConfig(long limitRefreshPeriod, int limitForPeriod, long timeoutDuration) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(limitRefreshPeriod))
                .limitForPeriod(limitForPeriod)
                .timeoutDuration(Duration.ofMillis(timeoutDuration))
                .build();
        return config;
    }

    public RateLimiter getRateLimiter(String name, long limitRefreshPeriod, int limitForPeriod, long timeoutDuration) {
        RateLimiterRegistry registry = RateLimiterRegistry.of(getRateLimiterConfig(limitRefreshPeriod, limitForPeriod, timeoutDuration));
        registry.getEventPublisher()
                .onEntryAdded(event -> log.info("RateLimiter added: {}", event.getAddedEntry().getName()))
                .onEntryRemoved(event -> log.info("RateLimiter removed: {}", event.getRemovedEntry().getName()));
        return registry.rateLimiter(name);
    }
}
