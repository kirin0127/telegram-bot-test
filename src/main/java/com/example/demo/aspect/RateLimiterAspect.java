package com.example.demo.aspect;

import com.example.demo.annotation.RateLimited;
import com.example.demo.util.RateLimiterFactory;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class RateLimiterAspect {
    private final RateLimiterFactory rateLimiterFactory;

    private final Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    @Pointcut("@annotation(rateLimited)")
    public void rateLimitedMethods(RateLimited rateLimited) {

    }

    @Around("rateLimitedMethods(rateLimited)")
    public void beforeExecution(ProceedingJoinPoint joinPoint, RateLimited rateLimited) {
        RateLimiter rateLimiter = map.computeIfAbsent(rateLimited.name(), (name) -> rateLimiterFactory.getRateLimiter(
                name,
                rateLimited.limitRefreshPeriod(),
                rateLimited.limitForPeriod(),
                rateLimited.timeoutDuration()
        ));
        map.putIfAbsent(rateLimited.name(), rateLimiter);
        CheckedRunnable call = RateLimiter.decorateCheckedRunnable(rateLimiter, joinPoint::proceed);
        Try.run(call).onFailure(throwable -> log.info("Wait before call it again :)"));
    }
}
