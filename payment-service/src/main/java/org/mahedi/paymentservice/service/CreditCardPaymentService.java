package org.mahedi.paymentservice.service;

import java.math.BigDecimal;

public interface CreditCardPaymentService {
    // wil be called by PaymentService-process method with creditCardNo and paymentAmount
    // this process method will call credit-card-processor-service via http call
    // payload: CreditCardProcessRequest
    // use a try-catch, throw Custom exception named: CreditCardProcessorUnavailableException
    public void process(String creditCardNo, BigDecimal paymentAmount);
}
