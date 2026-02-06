# Distributed Order Management System

A real-world, event-driven microservices system that models how large e-commerce platforms process orders using Kafka, Redis, and Spring Boot.
This project demonstrates how independent services communicate asynchronously to handle order creation, inventory reservation, payment processing, and customer notifications.

This project includes a lightweight interactive web UI built with Streamlit to visualize and test the entire distributed order flow in real time.
The UI allows you to place orders, track them, and observe how they move through Kafka-driven microservices — without reading logs. Use the demo UI like this

---

## Tech Stack

| Layer | Technology |
|------|-----------|
| Demo Frontend | Streamlit |
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

## Event Flow 

![Untitled Diagram (1)](https://github.com/user-attachments/assets/725d327d-8708-4c9b-a568-087c06770183)

All communication is **event-driven** — services never call each other directly.

## How to Run

### Use Demo UI

1. Running the UI
   
```
cd doms-ui
pip install -r requirements.txt
streamlit run app.py
```

Then open:

```
http://localhost:8501
```

2. Example Flow

Enter a User ID -> Add Product IDs & Quantities -> Click Place Order -> Click Refresh Orders -> Get Order Table

### Use CLI

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


3. Verify the Flow

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
