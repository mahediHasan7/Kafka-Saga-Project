# Kafka Orchestration-Based Saga Design Pattern

**Orchestrator**: Order Saga - Coordinates the saga workflow using Kafka

## Implementation Steps - Happy Path

### 1. Product Service

- Create a product

### 2. Order Service

- Create an order using product IDs
- Publish `OrderCreatedEvent` to `orders-events`

### 3. Order Saga

- Listen `OrderCreatedEvent` from `orders-events`
- Publish `ProductReserveCommand` to `products-commands`
- Save a new record in `orders-history` table with `CREATED` status

### 4. Product Service

- Listen `ProductReserveCommand` from `products-commands`
- Reserve products based on the message
- Publish `ProductReserveConfirmationEvent` to `products-events`
- or
- Publish `ProductReserveFailedEvent` to `products-events`

### 5. Order Saga

- Listen `ProductReserveFailedEvent` from `products-events`
- Publish `PaymentProcessCommand` to `payments-commands`

### 6. Payment Service

- Listen `PaymentProcessCommand` from `payments-commands`
- Save payment record in `payments` table
- Publish `PaymentProcessSuccessfulEvent` to `payments-events`

### 7. Order Saga

- Listen `PaymentProcessSuccessfulEvent` from `payments-events`
- Publish `OrderApprovedCommand` to `orders-commands`

### 8. Order Service

- Listen `OrderApprovedCommand` from `orders-commands`
- Change the order status to `APPROVED` in `orders` table
- Publish `OrderApprovedEvent` to `orders-events`

### 9. Order Saga

- Listen `OrderApprovedEvent` from `orders-events`
- Add a new record in `orders_history` table with order_id and status = `APPROVED`

## Implementation Steps - Compensating Path

10. Product Service (Compensating path: Product Reservation failed)

- Publish `ProductReserveCancelledEvent` to `products-events`

11. Order Saga (Compensating path: Product Reservation failed)

- Listen `ProductReserveCancelledEvent` from `products-events`
- Publish `OrderRejectCommand` to `order-commands`
- Add a new record in `orders_history` table with order_id and status = `REJECTED`

12. Order Service (Compensating path: Product Reservation failed)

- Listen `OrderRejectCommand` from `order-commands`
- Change the order status to `REJECTED` in `orders` table

13. Payment Service(Compensating path: Payment Failed)

- Publish `PaymentFailedEvent` to `payments-events`

14. Order Saga (Compensating path: Payment Failed)

- Listen `PaymentFailedEvent` from `payments-events`
- Publish `ProductReserveCancelCommand` to `products-commands`

15. Product Service (Compensating path: Payment Failed)

- Listen `ProductReserveCancelCommand` to `products-commands`
- Reverse product reservation by updating product quantity
- Publish `ProductReserveCancelledEvent` to `products-events`

16. Order Saga (Compensating path: Payment Failed)

- Same as 11

17. Order Service (Compensating path: Payment Failed)

- Same as 12

