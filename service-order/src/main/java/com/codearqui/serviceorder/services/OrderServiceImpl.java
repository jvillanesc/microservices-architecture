package com.codearqui.serviceorder.services;

import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import com.codearqui.serviceorder.model.entity.OrderModel;
import com.codearqui.serviceorder.model.mapper.OrderMapper;
import com.codearqui.serviceorder.proxy.api.InventoryApi;
import com.codearqui.serviceorder.proxy.model.InventoryResponse;
import com.codearqui.serviceorder.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryApi inventoryApi;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final TimeLimiter timeLimiter;
    private final RateLimiter rateLimiter;

    @Override
    public Mono<OrderResponse> createOrder(OrderRequest orderRequest) {
        var orderData = OrderMapper.INSTANCE.requestToModel(orderRequest);
        orderData.setDateOrder(LocalDateTime.now().toString());
        orderData.setStatusOrder(OrderResponse.StatusEnum.PENDING.getValue());

        return orderRepository.save(orderData)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Mono<OrderResponse> getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<OrderResponse> getListOrders() {
        return orderRepository.findAll()
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Mono<OrderResponse> updateOrder(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatusOrder(OrderResponse.StatusEnum.COMPLETED.getValue());
                    return order;
                })
                .flatMap(order -> invokeService(order)
                        .switchIfEmpty(Mono.empty())
                        .map(x -> order)
                )
                .flatMap(orderRepository::save)
                .map(OrderMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(p -> log.info("Updating order with ID: {}", orderId))
                .doOnSuccess(p -> log.info("Order with ID: {} updated successfully", orderId))
                .doOnError(ex -> log.error("Error updating order with ID: {}", orderId, ex));
    }

    private Mono<InventoryResponse> invokeService(OrderModel value) {
        return inventoryApi.getInventory(value.getCodeProduct())
//                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))

              //  .transformDeferred(RetryOperator.of(retry))
                .transformDeferred(TimeLimiterOperator.of(timeLimiter))
//                .transformDeferred(RateLimiterOperator.of(rateLimiter))
                .onErrorResume(CallNotPermittedException.class, e -> fallbackInventory(value))
                .doOnSubscribe(p -> log.info("Invoking inventory service for product code: {}", value.getCodeProduct()))
                .doOnSuccess(p -> log.info("Successfully fetched inventory for product code: {}", value.getCodeProduct()))
                .doOnError(ex -> log.error("Error fetching inventory for product code: {}", value.getCodeProduct(), ex));
    }

    private Mono<InventoryResponse> fallbackInventory(OrderModel value) {
        log.warn("Fallback triggered for product code: {}", value.getCodeProduct());
        return Mono.error(new RuntimeException("Inventory service is currently unavailable. Please try again later."));
    }
}
