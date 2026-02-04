/*package com.doms.orderservice.events;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderCreatedEvent {

    private UUID orderId;
    private UUID userId;
    private BigDecimal totalAmount;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(UUID orderId, UUID userId, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
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
}*/

package com.doms.orderservice.events;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * This event represents a complete order with product + quantity mapping.
 * Inventory Service uses this to reserve stock.
 */
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

