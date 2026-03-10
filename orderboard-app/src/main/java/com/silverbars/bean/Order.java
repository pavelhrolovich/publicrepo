package com.silverbars.bean;

import com.silverbars.enums.OrderType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Container of Order information
 */
public class Order {

    private final long orderId;
    private final String user;
    private final double quantity;
    private final BigDecimal price;
    private final OrderType orderType;
    private char active;

    /**
     * I have added this basic Order Audit here to keep track of users who create and cancels orders.
     * In Prod application this entity would probably have its own workflow for CRUD operations and
     * probably this orderAudits object will not be part of this Order object
     */
    private final Collection<OrderAudit> orderAudits = new ArrayList<>();

    public Order(long orderId, String user, double quantity, BigDecimal price, OrderType orderType) {
        this.orderId = orderId;
        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
        this.active = 'Y';
    }

    public long getOrderId() {
        return orderId;
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

    public char getActive() {
        return active;
    }

    public void markOrderInActive() {
        this.active = 'N';
    }

    public Collection<OrderAudit> getOrderAudits() {
        return orderAudits;
    }

    public void addOrderAudit(OrderAudit orderAudit) {
        this.orderAudits.add(orderAudit);
    }

    // TODO: Add equals and hashcode

}


