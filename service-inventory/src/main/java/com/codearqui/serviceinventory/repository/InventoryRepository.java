package com.codearqui.serviceinventory.repository;

import com.codearqui.serviceinventory.model.entity.ProductsModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface InventoryRepository extends R2dbcRepository<ProductsModel, Integer> {
    Mono<Boolean> existsByCode(String code);
    Mono<ProductsModel> findByCode(String code);
}
