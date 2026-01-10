package org.mahedi.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class CreditCardProcessRequest {
    private String creditCardNo;
    private BigDecimal paymentAmount;
}
