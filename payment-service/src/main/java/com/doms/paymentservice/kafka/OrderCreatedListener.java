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
        topics = "inventory.reserved",
        groupId = "payment-group"
    )
    public void handleInventoryReserved(String message) {
        // message contains productId + quantity
        paymentService.processPayment(message);
    }
}

