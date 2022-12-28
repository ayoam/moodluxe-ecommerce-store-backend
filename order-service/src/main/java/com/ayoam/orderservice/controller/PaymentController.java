package com.ayoam.orderservice.controller;

import com.ayoam.orderservice.dto.StripeChargeRequest;
import com.ayoam.orderservice.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/stripe/charge")
    public ResponseEntity<?> stripeCharge(@RequestBody StripeChargeRequest stripeChargeRequest) throws StripeException {
        return paymentService.stripeCharge(stripeChargeRequest);
    }
}
