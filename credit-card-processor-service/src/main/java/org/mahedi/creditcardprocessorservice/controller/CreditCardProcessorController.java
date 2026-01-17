package org.mahedi.creditcardprocessorservice.controller;

import jakarta.validation.Valid;
import org.mahedi.core.dto.CreditCardProcessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ccp")
public class CreditCardProcessorController {
    private final Logger LOGGER = LoggerFactory.getLogger(CreditCardProcessorController.class);

    @PostMapping("/process")
    public void processCreditCard(@RequestBody @Valid CreditCardProcessRequest payload) {
        LOGGER.info("Processing request: {}", payload);
    }

}
