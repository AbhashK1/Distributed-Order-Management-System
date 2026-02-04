package com.doms.inventoryservice.kafka;

import java.util.Iterator;
import java.util.Map;
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
            JsonNode root = objectMapper.readTree(message);

            UUID orderId = UUID.fromString(root.get("orderId").asText());
            JsonNode itemsNode = root.get("items");

            if (itemsNode == null || !itemsNode.isObject()) {
                throw new IllegalStateException("Order does not contain items");
            }

            // Loop over productId -> quantity
            Iterator<Map.Entry<String, JsonNode>> fields = itemsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();

                UUID productId = UUID.fromString(entry.getKey());
                int quantity = entry.getValue().asInt();

                inventoryService.reserveStock(productId, quantity);
            }

            System.out.println("Inventory reserved for order " + orderId);

        } catch (Exception e) {
            System.err.println("Failed to process order.created message: " + message);
            e.printStackTrace();
        }
    }
}

