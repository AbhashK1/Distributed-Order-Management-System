# Distributed Order Management System

A real-world, event-driven microservices system that models how large e-commerce platforms process orders using Kafka, Redis, and Spring Boot.
This project demonstrates how independent services communicate asynchronously to handle order creation, inventory reservation, payment processing, and customer notifications.

## System Architecture




All communication is **event-driven** — services never call each other directly.

---

## Tech Stack

| Layer | Technology |
|------|-----------|
| Backend | Spring Boot (Java) |
| Messaging | Apache Kafka |
| Cache / Inventory | Redis |
| Database | PostgreSQL |
| Containerization | Docker + Docker Compose |
| Serialization | JSON (Jackson) |

---

## Microservices

### 1️⃣ Order Service
- Creates new orders
- Publishes `order.created` events
- Stores orders in PostgreSQL

### 2️⃣ Inventory Service
- Consumes `order.created`
- Reserves stock in Redis
- Publishes `inventory.reserved`

### 3️⃣ Payment Service
- Consumes `inventory.reserved`
- Simulates payment success/failure
- Publishes `payment.success` or `payment.failed`

### 4️⃣ Notification Service
- Consumes payment events
- Logs success or failure notifications

---

## Order Event Format

```json
{
  "orderId": "uuid",
  "userId": "uuid",
  "totalAmount": 500,
  "items": {
    "product-id-1": 2,
    "product-id-2": 1
  }
}
```

How to Run

1. Start the system

```
docker compose up --build
```

This launches:

 - Kafka
 - Zookeeper
 - Redis
 - PostgreSQL
 - All microservices

2. Create an order
   
```
curl -X POST http://localhost:8081/orders \
-H "Content-Type: application/json" \
-d '{
  "userId": "11111111-1111-1111-1111-111111111111",
  "amount": 500,
  "items": {
    "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa": 2,
    "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb": 1
  }
}'
```

Event Flow 

![Untitled Diagram (1)](https://github.com/user-attachments/assets/725d327d-8708-4c9b-a568-087c06770183)

Verify the Flow

Watch logs:

```
docker logs distributed-order-management-inventory-service-1
docker logs distributed-order-management-payment-service-1
docker logs distributed-order-management-notification-service-1
```

You will see:

```
Inventory reserved
Payment success
Notification sent
``` 

---

## Key Design Patterns Used

 - Event-Driven Architecture
 - Saga-style workflow
 - Loose coupling via Kafka
 - Idempotent message handling
 - Redis atomic operations
 - Asynchronous fault-tolerant processing


Author:
Abhash Kumar
