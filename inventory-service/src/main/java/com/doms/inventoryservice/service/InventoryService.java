/*package com.doms.inventoryservice.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doms.inventoryservice.model.Inventory;
import com.doms.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repo;
    private final RedisTemplate<String, Integer> redis;

    public int getStock(UUID productId) {
        String key = "inventory:" + productId;

        Integer cached = redis.opsForValue().get(key);
        if (cached != null) return cached;

        int stock = repo.findById(productId)
                        .orElseThrow()
                        .getAvailableQuantity();

        redis.opsForValue().set(key, stock, 10, TimeUnit.MINUTES);
        return stock;
    }

    @Transactional
    public void reserveStock(UUID productId, int qty) {
        Inventory inv = repo.findById(productId).orElseThrow();
        inv.setAvailableQuantity(inv.getAvailableQuantity() - qty);
        repo.save(inv);
        redis.delete("inventory:" + productId);
    }
}*/

//Second Change
/*package com.doms.inventoryservice.service;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final RedisTemplate<String, Integer> redis;
    private final KafkaTemplate<String, Object> kafka;

    private static final String INVENTORY_KEY_PREFIX = "inventory:";

    /**
     * Reserve stock for a given product.
     *
     * @param productId UUID of the product
     * @param quantity Quantity to reserve
     * @throws IllegalStateException if stock is insufficient
     */
    /*public void reserveStock(UUID productId, int quantity) {
        String key = INVENTORY_KEY_PREFIX + productId;

        Integer currentStock = redis.opsForValue().get(key);
        if (currentStock == null) {
            throw new IllegalStateException("Product not found in inventory: " + productId);
        }

        if (currentStock < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + productId);
        }

        // Decrement stock atomically
        int updatedStock = redis.opsForValue().decrement(key, quantity).intValue();

        System.out.println("Reserved " + quantity + " units for product " + productId + ". Remaining stock: " + updatedStock);

        // Optionally publish an event to notify other services
        kafka.send("inventory.reserved", new InventoryReservedEvent(productId, quantity));
    }

    /**
     * Event object to send to Kafka after reservation.
     */
    /*@RequiredArgsConstructor
    public static class InventoryReservedEvent {
        private final UUID productId;
        private final int quantity;

        public UUID getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}*/

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

    //private static final String INVENTORY_KEY_PREFIX = 'inventory:';
    private static final String INVENTORY_KEY_PREFIX="inventory:";

    public void reserveStock(UUID productId, int quantity) {
        String key = INVENTORY_KEY_PREFIX + productId;

        Object raw = redis.opsForValue().get(key);

        if (raw == null) {
            throw new IllegalStateException("Product not found in inventory: " + productId);
        }

        int currentStock = Integer.parseInt(raw.toString());

        if (currentStock < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + productId);
        }

        // Atomic decrement
        Long updated = redis.opsForValue().decrement(key, quantity);

        System.out.println("Reserved " + quantity + " units for product " + productId +". Remaining stock: " + updated);

        kafka.send("inventory.reserved",new InventoryReservedEvent(productId, quantity));
    }

    /**
     * Kafka Event
     */
    @RequiredArgsConstructor
    public static class InventoryReservedEvent {
        private final UUID productId;
        private final int quantity;

        public UUID getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}


