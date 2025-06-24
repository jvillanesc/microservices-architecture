package com.codearqui.serviceinventory.service;


import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import com.codearqui.serviceinventory.model.mapper.InventoryMapper;
import com.codearqui.serviceinventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    public Mono<InventoryResponse> createOrder(Mono<InventoryRequest> inventory) {
        return inventory.map(InventoryMapper.INSTANCE::requestToModel)
                .flatMap(value -> inventoryRepository.existsByCode(value.getCode())
                        .flatMap(y -> {
                            if (y) {
                                return Mono.empty();
                            }
                            return inventoryRepository.save(value);
                        })
                )
                .map(InventoryMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(p -> log.info("Subscribe to create order"))
                .doOnSuccess(p -> log.info("Order created successfully with code: {}", p.getIdProduct()))
                .doOnError(e -> log.error("Error creating order", e.getMessage()));
    }

    @Override
    public Mono<InventoryResponse> getInventory(String code) {
        return inventoryRepository.findByCode(code)
                .map(InventoryMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<InventoryResponse> getList() {
        return inventoryRepository.findAll()
                .map(InventoryMapper.INSTANCE::modelToResponse);
    }
}
