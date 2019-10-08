package com.sgh.demo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class PaymentDto {

    @Size(min = 1)
    private final List<String> invoices;

    @DecimalMin("0.01")
    private final BigDecimal amount;

    private final String currency;
    private final String email;

    @JsonCreator
    public PaymentDto(@JsonProperty("invoices") List<String> invoices, @JsonProperty("amount") BigDecimal amount,
                      @JsonProperty("currency") String currency, @JsonProperty("email") String email) {
        super();
        this.invoices = invoices;
        this.amount = amount;
        this.currency = currency;
        this.email = email;
    }

    public List<String> getInvoices() {
        return invoices;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getEmail() {
        return email;
    }
}
