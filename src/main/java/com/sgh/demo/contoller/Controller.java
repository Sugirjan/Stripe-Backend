package com.sgh.demo.contoller;

import com.sgh.demo.dto.PaymentDto;
import com.sgh.demo.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/stripe")
public class Controller {

    private final PaymentService paymentService;

    public Controller(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Integer> createCharge(@RequestParam String token, @RequestParam String amount)
            throws StripeException {
        return ResponseEntity.ok(paymentService.getCharge(token, amount));
    }

}
