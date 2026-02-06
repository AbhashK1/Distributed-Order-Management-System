package com.doms.orderservice.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doms.orderservice.events.OrderCreatedEvent;
import com.doms.orderservice.model.Order;
import com.doms.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    //create order and publish a complete OrderCreatedEvent
    //containing product → quantity mapping for Inventory Service
    @Transactional
    public Order createOrder(
            UUID userId,
            BigDecimal amount,
            Map<UUID, Integer> items
        ) {
        try {
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(amount);
            order.setStatus("CREATED");
            order.setItems(items); //saved items here

            order = orderRepository.save(order);  // get persisted order with ID

            //Kafka event from DB-backed order
            OrderCreatedEvent event = new OrderCreatedEvent(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalAmount(),
                    order.getItems()
            );

            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order.created", payload);

            return order;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public Page<Order> getOrders(UUID userId, int page, int size) {
        return orderRepository.findByUserId(
                userId,
                PageRequest.of(page, size)
        );
    }
}

