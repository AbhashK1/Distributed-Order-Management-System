package com.doms.paymentservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.doms.paymentservice.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final PaymentService paymentService;

    @KafkaListener(
        topics = "order.created",
        groupId = "payment-group"
    )
    public void handleOrderCreated(String orderId) {
        paymentService.processPayment(orderId);
    }
}
