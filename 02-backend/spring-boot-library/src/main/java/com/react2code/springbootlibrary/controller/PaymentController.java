package com.react2code.springbootlibrary.controller;

import com.react2code.springbootlibrary.requestModel.PaymentInfoRequest;
import com.react2code.springbootlibrary.service.PaymentService;
import com.react2code.springbootlibrary.utils.ExtractJwt;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/payment/secure")
@RestController
@CrossOrigin("https://localhost:3000")
public class PaymentController {

    private PaymentService paymentService;

    public static final String email = "\"sub\"";

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws StripeException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);

    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJwt.extractJwtPayload(token, email);
        if (userEmail == null) {
            throw new Exception("UserEmail is missing");
        }
        return paymentService.stripePayment(userEmail);

    }
}
