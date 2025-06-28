package com.codearqui.serviceorder.services;

import com.codearqui.serviceorder.dto.OrderRequest;
import com.codearqui.serviceorder.dto.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> createOrder(OrderRequest orderRequest);
    Mono<OrderResponse> getOrderById(int orderId);
    Flux<OrderResponse> getListOrders();
    Mono<OrderResponse> updateOrder(int orderId);
}
