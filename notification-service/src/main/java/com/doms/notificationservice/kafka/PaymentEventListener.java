package com.doms.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.doms.notificationservice.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final NotificationService notificationService;

    @KafkaListener(
        topics = "payment.success",
        groupId = "notification-group"
    )
    public void handleSuccess(String orderId) {
        notificationService.sendSuccess(orderId);
    }

    @KafkaListener(
        topics = "payment.failed",
        groupId = "notification-group"
    )
    public void handleFailure(String orderId) {
        notificationService.sendFailure(orderId);
    }
}
