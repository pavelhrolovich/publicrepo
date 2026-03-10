package com.silverbars.bean;

import com.silverbars.enums.OrderType;

import java.math.BigDecimal;

public class OrderRequest {

    private String user;
    private double quantity;
    private BigDecimal price;
    private OrderType orderType;

    // Needed for Jackson
    public OrderRequest() {
    }

    public OrderRequest(String user, double quantity, BigDecimal price, OrderType orderType) {
        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public String getUser() {
        return user;
    }

    public double getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
