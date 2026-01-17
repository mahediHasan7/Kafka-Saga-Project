package org.mahedi.paymentservice.service;

import org.mahedi.core.dto.CreditCardProcessRequest;
import org.mahedi.core.exceptions.CreditCardProcessorUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CreditCardPaymentServiceImpl implements CreditCardPaymentService {
    private final RestTemplate restTemplate;
    private final String ccpRemoteUrl;

    public CreditCardPaymentServiceImpl(RestTemplate restTemplate, @Value("${remote.ccp.url}") String ccpRemoteUrl) {
        this.restTemplate = restTemplate;
        this.ccpRemoteUrl = ccpRemoteUrl;
    }

    @Override
    public void process(String creditCardNo, BigDecimal paymentAmount) {
        try {
            CreditCardProcessRequest ccpRequest = new CreditCardProcessRequest(creditCardNo, paymentAmount);
            restTemplate.postForObject(ccpRemoteUrl + "/ccp/process", ccpRequest, CreditCardProcessRequest.class);
        } catch (ResourceAccessException e) {
            throw new CreditCardProcessorUnavailableException(e);
        }
    }
}
