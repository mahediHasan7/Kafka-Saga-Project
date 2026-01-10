package org.mahedi.paymentservice.service;

import org.mahedi.core.events.OrderCreatedEvent;
import org.mahedi.paymentservice.dto.Payment;
import org.mahedi.paymentservice.entity.PaymentEntity;
import org.mahedi.paymentservice.repository.PaymentRepository;
import org.springframework.beans.BeanUtils;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CreditCardPaymentService creditCardPaymentService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardPaymentService creditCardPaymentService) {
        this.paymentRepository = paymentRepository;
        this.creditCardPaymentService = creditCardPaymentService;
    }

    @Override
    public Payment process(OrderCreatedEvent orderCreatedEvent) {
        String SAMPLE_CREDIT_CARD_NUMBER = "1344-4845-348589";
        creditCardPaymentService.process(SAMPLE_CREDIT_CARD_NUMBER, orderCreatedEvent.getTotalPrice());

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setOrderId(orderCreatedEvent.getOrderId());
        paymentEntity.setPaymentAmount(orderCreatedEvent.getTotalPrice());

        PaymentEntity saved = paymentRepository.save(paymentEntity);

        Payment payment = new Payment();
        BeanUtils.copyProperties(saved, payment);
        return payment;
    }
}
