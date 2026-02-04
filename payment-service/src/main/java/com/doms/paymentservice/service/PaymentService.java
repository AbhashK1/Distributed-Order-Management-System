package com.doms.paymentservice.service;

import java.util.Random;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Random random = new Random();

    public void processPayment(String orderId) {

        boolean success = random.nextInt(10) < 8; // 80% success

        if (success) {
            kafkaTemplate.send("payment.success", orderId);
        } else {
            throw new RuntimeException("Payment failed");
        }
    }
}
