package com.codearqui.serviceinventory.controller;

import com.codearqui.serviceinventory.api.InventoryApiController;
import com.codearqui.serviceinventory.dto.InventoryResponse;
import com.codearqui.serviceinventory.expose.InventoryApiImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(InventoryApiController.class)
@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private InventoryApiImpl inventoryApiImpl;

    @Test
    @DisplayName("get list inventory when product exists then return 200")
    void getListInventoryWhenProductExistsThenReturn200() {
        // Arrange
        var inventory = new InventoryResponse();
        inventory.setIdProduct("1");

        when(inventoryApiImpl.listInventory(any()))
                .thenReturn(Flux.just(inventory));

        // Act
        webTestClient.get()
                .uri("/services-inventory/inventories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InventoryResponse.class)
                .contains(inventory);

        // Assert
        verify(inventoryApiImpl, times(1)).listInventory(any());
    }
}
