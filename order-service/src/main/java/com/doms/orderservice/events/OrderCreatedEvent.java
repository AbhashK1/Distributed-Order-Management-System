package com.doms.orderservice.events;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class OrderCreatedEvent {

    private UUID orderId;
    private UUID userId;
    private BigDecimal totalAmount;

    // productId -> quantity
    private Map<UUID, Integer> items;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(UUID orderId, UUID userId, BigDecimal totalAmount, Map<UUID, Integer> items) 
    {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Map<UUID, Integer> getItems() {
        return items;
    }
}

