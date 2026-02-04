package com.doms.inventoryservice.service;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final RedisTemplate<String, Object> redis;
    private final KafkaTemplate<String, Object> kafka;

    private static final String INVENTORY_KEY_PREFIX = "inventory:";
    private static final int DEFAULT_STOCK = 100;

    //reserve stock for product using Redis atomic operations
    public void reserveStock(UUID productId, int quantity) {
        String key = INVENTORY_KEY_PREFIX + productId;

        redis.opsForValue().setIfAbsent(key, DEFAULT_STOCK);

        //decrement
        Long remaining = redis.opsForValue().decrement(key, quantity);

        if (remaining == null) {
            throw new IllegalStateException("Redis failure for product " + productId);
        }

        //stock negative → rollback & fail
        if (remaining < 0) {
            redis.opsForValue().increment(key, quantity); // rollback
            throw new IllegalStateException(
                "Insufficient stock for product " + productId +
                ". Requested=" + quantity +
                ", Available=" + (remaining + quantity)
            );
        }

        System.out.println(
            "Reserved " + quantity +
            " units for product " + productId +
            ". Remaining stock: " + remaining
        );

        //kafka.send("inventory.reserved", new InventoryReservedEvent(productId, quantity));
        try {
            String json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(new InventoryReservedEvent(productId, quantity));

            kafka.send("inventory.reserved", json);

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish inventory.reserved event", e);
        }
    }

    //Kafka Event sent after stock is successfully reserved
    public static class InventoryReservedEvent {
        private final UUID productId;
        private final int quantity;

        public InventoryReservedEvent(UUID productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public UUID getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}




