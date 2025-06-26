package com.codearqui.serviceinventory.service;

import com.codearqui.serviceinventory.dto.InventoryRequest;
import com.codearqui.serviceinventory.model.entity.ProductsModel;
import com.codearqui.serviceinventory.model.mapper.InventoryMapper;
import com.codearqui.serviceinventory.repository.InventoryRepository;
import com.codearqui.serviceinventory.service.InventoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServicesImplTest {
    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("create order when parameters is correct then return sucess")
    void createOrder_whenParametersIsCorrect_thenReturnSuccess() {
        // Arrange
        var requestInventory = new InventoryRequest()
                .idProduct("123")
                .nameProduct("Lentes")
                .price(BigDecimal.TEN)
                .stock(10);
        var productModel = ProductsModel.builder()
                .id(1)
                .code("123")
                .nameProduct("Lentes")
                .price(BigDecimal.TEN)
                .build();
        var responseInventory = InventoryMapper.INSTANCE.modelToResponse(productModel);

        when(inventoryRepository.existsByCode(anyString()))
                .thenReturn(Mono.just(false));
        when(inventoryRepository.save(any(ProductsModel.class)))
                .thenReturn(Mono.just(productModel));

        // Act
        var result = inventoryService.createOrder(Mono.just(requestInventory));

        // Assert
        StepVerifier.create(result)
                .expectNext(responseInventory)
                .verifyComplete();

        verify(inventoryRepository, times(1)).existsByCode("123");
    }
}
