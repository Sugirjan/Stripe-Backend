package com.sgh.demo.service;

import com.sgh.demo.dto.PaymentDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
public class PaymentService {
    private static final String CONNECTED_STRIPE_ACCOUNT_ID = ""; //
    private static final String SECRET_API_KEY = ""; //

    private static Customer customer;

    /*
    get Charge without any application fees
     */
    public int getCharge(String token, String amount) throws StripeException {
        Stripe.apiKey = SECRET_API_KEY;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "lkr");
        params.put("description", "Example charge");
        params.put("source", token);
        params.put("statement_descriptor", "Custom descriptor");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", "6735");
        params.put("metadata", metadata);
        Charge charge = Charge.create(params);
        return new Random().nextInt();
    }

    /*
    Create test account
     */
    private static Account getAccount() throws StripeException {
        Stripe.apiKey = SECRET_API_KEY;

        Map<String, Object> params = new HashMap<>();
        params.put("country", "US");
        params.put("type", "custom");
        params.put("requested_capabilities", Arrays.asList("card_payments", "transfers"));
        return Account.create(params);
    }

    /*
    Create Customer
    */
    private static Customer getCustomer(String token) throws StripeException {
        Stripe.apiKey = SECRET_API_KEY;

        if (Objects.isNull(customer)) {
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Customer for sugirjan.r@example.com");
            customerParams.put("source", token);
            customerParams.put("name", "Sugirjan");
            customerParams.put("email", "sugirjan.r@example.com");
            customer = Customer.create(customerParams);
        }
        return customer;
    }

    /*
    Create Charge through Stripe Connect
     */
    public String getConnectCharge(String token, PaymentDto paymentDto) throws StripeException {
        Stripe.apiKey = SECRET_API_KEY;

        BigDecimal feeRate = new BigDecimal("0.025");
        BigDecimal applicationFee = paymentDto.getAmount().multiply(feeRate).setScale(2, RoundingMode.HALF_EVEN);
        String invoices = String.join(", ", paymentDto.getInvoices());

        Map<String, Object> params = new HashMap<>();
        params.put("amount", this.getStripeAmount(paymentDto.getAmount()));
        params.put("currency", paymentDto.getCurrency());
        params.put("source", token);
        params.put("application_fee_amount", this.getStripeAmount(applicationFee));
        params.put("description", "Charges for invoices " + invoices);
        params.put("receipt_email", paymentDto.getEmail());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Charges for invoices ", invoices);
        params.put("metadata", metadata);

        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(CONNECTED_STRIPE_ACCOUNT_ID).build();
        Charge charge = Charge.create(params, requestOptions);
        return charge.toJson();

    }

    /*
    Get amount for Stripe
     */
    private String getStripeAmount(BigDecimal value) {
        String[] amountList = value.toString().split("\\.");
        return amountList[0] + amountList[1];
    }
}
