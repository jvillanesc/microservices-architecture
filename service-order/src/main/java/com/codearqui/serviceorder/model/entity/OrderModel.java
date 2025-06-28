package com.codearqui.serviceorder.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("order_shop")
public class OrderModel {
    @Id
    private Integer id;

    @Column("code_product")
    private String codeProduct;

    @Column("date_order")
    private String dateOrder;

    @Column("status_order")
    private String statusOrder;
}
