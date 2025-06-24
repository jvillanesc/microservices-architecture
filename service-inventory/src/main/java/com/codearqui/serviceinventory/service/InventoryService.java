package com.codearqui.serviceinventory.service;

import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryService {
    Mono<InventoryResponse> createOrder(Mono<InventoryRequest> inventory);
    Mono<InventoryResponse> getInventory(String code);
    Flux<InventoryResponse> getList();
}
