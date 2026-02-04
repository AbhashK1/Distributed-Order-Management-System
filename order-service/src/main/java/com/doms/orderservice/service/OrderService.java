/*package com.doms.orderservice.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doms.orderservice.model.Order;
import com.doms.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public Order createOrder(UUID userId, BigDecimal amount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(amount);
        order.setStatus("CREATED");

        orderRepository.save(order);
        kafkaTemplate.send("order.created", order.getId().toString());

        return order;
    }

    public Page<Order> getOrders(UUID userId, int page, int size) {
        return orderRepository.findByUserId(
            userId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }
}*/

//Second Change
/*package com.doms.orderservice.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public Order createOrder(UUID userId, BigDecimal amount) {
        try {
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(amount);
            order.setStatus("CREATED");

            orderRepository.save(order);

            // 🔥 Create Kafka event
            OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount()
            );

            // 🔥 Convert to JSON
            String payload = objectMapper.writeValueAsString(event);

            // 🔥 Send JSON to Kafka
            kafkaTemplate.send("order.created", payload);

            return order;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public Page<Order> getOrders(UUID userId, int page, int size) {
        return orderRepository.findByUserId(
            userId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }
}*/

package com.doms.orderservice.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public Order createOrder(UUID userId, BigDecimal amount) {
        try {
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(amount);
            order.setStatus("CREATED");

            orderRepository.save(order);

            // ✅ Create strongly-typed Kafka event
            OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount()
            );

            // ✅ Serialize to JSON
            String payload = objectMapper.writeValueAsString(event);

            // ✅ Send JSON to Kafka
            kafkaTemplate.send("order.created", payload);

            return order;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public Page<Order> getOrders(UUID userId, int page, int size) {
        return orderRepository.findByUserId(
            userId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }
}


