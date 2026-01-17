package org.mahedi.paymentservice.service;

import org.mahedi.core.events.OrderCreatedEvent;
import org.mahedi.core.exceptions.CreditCardProcessorUnavailableException;
import org.mahedi.paymentservice.dto.Payment;
import org.mahedi.paymentservice.entity.PaymentEntity;
import org.mahedi.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CreditCardPaymentService creditCardPaymentService;
    private Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardPaymentService creditCardPaymentService) {
        this.paymentRepository = paymentRepository;
        this.creditCardPaymentService = creditCardPaymentService;
    }

    @Override
    public Payment process(OrderCreatedEvent orderCreatedEvent) {
        String SAMPLE_CREDIT_CARD_NUMBER = "1344-4845-348589";
        try {
            creditCardPaymentService.process(SAMPLE_CREDIT_CARD_NUMBER, orderCreatedEvent.getTotalAmount());
        } catch (Exception e) {
            LOGGER.info("Credit card process issue: {}", e.getMessage());
            throw new CreditCardProcessorUnavailableException(e);
        }

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setOrderId(orderCreatedEvent.getOrderId());
        paymentEntity.setPaymentAmount(orderCreatedEvent.getTotalAmount());

        PaymentEntity saved = paymentRepository.save(paymentEntity);

        Payment payment = new Payment();
        BeanUtils.copyProperties(saved, payment);
        return payment;
    }
}
