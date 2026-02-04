package com.doms.orderservice.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doms.orderservice.model.Order;
import com.doms.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order create(@RequestParam UUID userId, @RequestParam BigDecimal amount) {
        return orderService.createOrder(userId, amount);
    }

    @GetMapping
    public Page<Order> history(@RequestParam UUID userId, @RequestParam int page, @RequestParam int size) {
        return orderService.getOrders(userId, page, size);
    }
}
