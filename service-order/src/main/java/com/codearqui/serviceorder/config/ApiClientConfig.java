package com.codearqui.serviceorder.config;

import com.codearqui.serviceorder.proxy.ApiClient;
import com.codearqui.serviceorder.proxy.api.InventoryApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfig {
    @Value("${client-micro.micro-inventory.base-url}")
    private String url;

    @Bean
    public InventoryApi inventoryApi(ApiClient apiClient) {
        return new InventoryApi(apiClient);
    }

    @Bean
    public ApiClient apiClient(WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(url);
        return apiClient;
    }

    @Bean
    public  WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}
