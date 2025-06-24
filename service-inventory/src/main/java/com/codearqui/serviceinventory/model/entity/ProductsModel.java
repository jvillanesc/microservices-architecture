package com.codearqui.serviceinventory.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
public class ProductsModel {
    @Id
    private int id;
    private String code;
    @Column("nameProduct")
    private String nameProduct;
    private int stock;
    private BigDecimal price;
}
