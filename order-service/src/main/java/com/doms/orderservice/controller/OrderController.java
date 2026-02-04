package com.doms.orderservice.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /*
    order example:
    {
        "userId": "11111111-1111-1111-1111-111111111111",
        "amount": 500,
        "items": {
           "aaaaaaa1-1111-1111-1111-111111111111": 2,
           "bbbbbbb2-2222-2222-2222-222222222222": 1
        }
    }
    */
    @PostMapping
    public Order create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(
                request.getUserId(),
                request.getAmount(),
                request.getItems()
        );
    }

    @GetMapping
    public Page<Order> history(
            @RequestParam UUID userId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return orderService.getOrders(userId, page, size);
    }

    //DTO for order creation
    public static class CreateOrderRequest {
        private UUID userId;
        private BigDecimal amount;
        private Map<UUID, Integer> items;

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Map<UUID, Integer> getItems() {
            return items;
        }

        public void setItems(Map<UUID, Integer> items) {
            this.items = items;
        }
    }
}

