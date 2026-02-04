/*package com.doms.inventoryservice.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.doms.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;

    @KafkaListener(
        topics = "order.created",
        groupId = "inventory-group"
    )
    public void handleOrderCreated(String productId) {
        inventoryService.reserveStock(
            UUID.fromString(productId),
            1
        );
    }
}*/

//Change 2
/*package com.doms.inventoryservice.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.doms.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "order.created",
        groupId = "inventory-group"
    )
    public void handleOrderCreated(@Payload String message) {
        try {
            // Parse incoming JSON
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID productId = UUID.fromString(jsonNode.get("productId").asText());
            int quantity = jsonNode.has("quantity") ? jsonNode.get("quantity").asInt() : 1;

            // Reserve stock
            inventoryService.reserveStock(productId, quantity);

        } catch (IllegalStateException e) {
            System.err.println("Inventory error: " + e.getMessage());
            // Optionally send a message to a Kafka topic for failed reservations
        } catch (Exception e) {
            System.err.println("Failed to process order.created message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}*/

package com.doms.inventoryservice.kafka;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.doms.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.created", groupId = "inventory-group")
    public void handleOrderCreated(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);

            UUID orderId = UUID.fromString(json.get("orderId").asText());
            UUID userId = UUID.fromString(json.get("userId").asText());
            int quantity = 1; // hardcoded for now

            inventoryService.reserveStock(orderId, quantity);

            System.out.println("✅ Inventory reserved for order " + orderId);

        } catch (Exception e) {
            System.err.println("❌ Failed to process order.created message: " + message);
            e.printStackTrace();
        }
    }
}
