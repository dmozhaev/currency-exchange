package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRateDto {

    @JsonProperty("base_currency")
    private String baseCurrency;

    @JsonProperty("quote_currency")
    private String quoteCurrency;

    private BigDecimal quote;

    private LocalDate date;

    public ExchangeRateDto() {}

    public ExchangeRateDto(
            String baseCurrency, String quoteCurrency, BigDecimal quote, LocalDate date) {
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.quote = quote;
        this.date = date;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public BigDecimal getQuote() {
        return quote;
    }

    public void setQuote(BigDecimal quote) {
        this.quote = quote;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
