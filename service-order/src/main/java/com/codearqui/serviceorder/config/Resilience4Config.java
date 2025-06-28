package com.codearqui.serviceorder.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4Config {
    private static final String BACKEND_SERVICE = "microInventory";

    @Bean
    CircuitBreaker remittanceServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(BACKEND_SERVICE);
    }

    @Bean
    Retry remittanceServicesRetry(RetryRegistry registry) {
        return registry.retry(BACKEND_SERVICE);
    }

    @Bean
    TimeLimiter remittanceServiceTimeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter(BACKEND_SERVICE);
    }

    @Bean
    RateLimiter remittanceServiceRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter(BACKEND_SERVICE);
    }
}
