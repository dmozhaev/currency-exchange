package com.example.demo.dto;

import com.example.demo.enums.Currency;

import java.math.BigDecimal;

public class ConversionDto {

    private Currency sourceCurrency;

    private Currency targetCurrency;

    private BigDecimal amount;

    public ConversionDto() {}

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
