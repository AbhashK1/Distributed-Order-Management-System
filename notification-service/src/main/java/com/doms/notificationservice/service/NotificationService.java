package com.doms.notificationservice.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    public void sendSuccess(String orderId) {
        log.info("Sending SUCCESS notification for order {}", orderId);
    }

    public void sendFailure(String orderId) {
        log.info("Sending FAILURE notification for order {}", orderId);
    }
}
