package com.example.demo.service;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.enums.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.net.URI;

@Service
public class SwopExchangeRateService {
    private static final String API_URL = "https://swop.cx/rest/rates";
    private static final String API_KEY = "xxx";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SwopExchangeRateService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Cacheable(value = "exchangeRates", key = "#baseCurrency + '-' + #targetCurrency")
    public BigDecimal getExchangeRate(Currency baseCurrency, Currency targetCurrency) throws Exception {
        String query = API_URL + "/" + baseCurrency + "/" + targetCurrency;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("Authorization", "ApiKey " + API_KEY)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ExchangeRateDto exchangeRateDto = objectMapper.readValue(response.body(), ExchangeRateDto.class);

        return exchangeRateDto.getQuote();
    }
}
