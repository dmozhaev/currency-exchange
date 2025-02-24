package com.example.demo.service;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.enums.Currency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.net.URI;

@Service
public class SwopExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(SwopExchangeRateService.class);

    private static final String API_URL = "https://swop.cx/rest/rates";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public SwopExchangeRateService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.apiKey = System.getenv("API_KEY");
    }

    @Cacheable(value = "exchangeRates", key = "#baseCurrency + '-' + #targetCurrency")
    public BigDecimal getExchangeRate(Currency baseCurrency, Currency targetCurrency) throws IOException, IllegalStateException, SecurityException, InterruptedException {
        logger.info("Fetching exchange rate: {} -> {}", baseCurrency, targetCurrency);

        String query = API_URL + "/" + baseCurrency + "/" + targetCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .header("Authorization", "ApiKey " + apiKey)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                String errorBody = response.body();
                logger.error("Failed to fetch exchange rate: {} -> {}. Status: {}. Body: {}", baseCurrency, targetCurrency, statusCode, errorBody);

                if (statusCode == 401) {
                    throw new SecurityException("Unauthorized access - API key is invalid.");
                }
                if (statusCode == 403 && errorBody.contains("Please upgrade your account to perform this request")) {
                    throw new IllegalStateException("Feature not available in free tier account");
                }
                if (statusCode >= 500 && statusCode <= 599) {
                    throw new IOException("API service error (5xx) - Server issue.");
                }
                throw new IOException("Exchange rate API error - Status code: " + statusCode);
            }

            ExchangeRateDto exchangeRateDto = objectMapper.readValue(response.body(), ExchangeRateDto.class);

            logger.info("Exchange rate fetched successfully: {} -> {} = {}", baseCurrency, targetCurrency, exchangeRateDto.getQuote());

            return exchangeRateDto.getQuote();
        } catch (IOException | IllegalStateException | SecurityException | InterruptedException e) {
            logger.error("Error while fetching or processing the exchange rate: {}", e.getMessage());
            throw e;
        }
    }
}
