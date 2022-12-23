package com.ayoam.orderservice.service;

import com.ayoam.orderservice.dto.StripeChargeRequest;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;

@Service
public class PaymentService {
    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    public ResponseEntity<?> stripeCharge(StripeChargeRequest stripeChargeRequest) throws StripeException {
        Stripe.apiKey=stripeApiKey;
        try{
            PaymentIntentCreateParams createParams = new
                    PaymentIntentCreateParams.Builder()
                    .setCurrency("usd")
                    .setAmount((long) (stripeChargeRequest.getAmount() * 100))
                    .setPaymentMethod(stripeChargeRequest.getId())
                    .setDescription("order #1111")
                    .setConfirm(true)
                    .build();

            PaymentIntent intent = PaymentIntent.create(createParams);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(CardException cardException){
            return new ResponseEntity<>(cardException.getMessage(), HttpStatus.valueOf(cardException.getStatusCode()));
        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(500));
        }
    }
}
