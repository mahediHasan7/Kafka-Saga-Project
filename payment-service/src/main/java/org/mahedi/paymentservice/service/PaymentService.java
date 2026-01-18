package org.mahedi.paymentservice.service;

import org.mahedi.core.dto.events.OrderCreatedEvent;
import org.mahedi.paymentservice.dto.Payment;

public interface PaymentService {
    // get order id and total price from orders-events via OrderCreatedEvent
    // call creditCardPayment service->process method with a credit card no and totalPrice, if fails, then throw a runtime exception
    // save the payment in db
    // return processedPayment with the paymentId, OrderId,TotalPrice etc...
    public Payment process(OrderCreatedEvent orderCreatedEvent);

}
