package com.codearqui.serviceorder.repository;

import com.codearqui.serviceorder.model.entity.OrderModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepository extends R2dbcRepository<OrderModel, Integer> {
}
