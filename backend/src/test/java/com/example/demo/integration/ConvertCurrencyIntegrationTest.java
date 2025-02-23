package com.example.demo.integration;

import com.example.demo.controller.ConversionController;
import com.example.demo.dto.ConversionDto;
import com.example.demo.enums.Currency;
import com.example.demo.service.SwopExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConversionController.class)
@WithMockUser
public class ConvertCurrencyIntegrationTest {

    private static final String URL = "/convert/";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SwopExchangeRateService swopExchangeRateService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void validRequestSuccess() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.EUR);
        request.setTargetCurrency(Currency.USD);
        request.setAmount(new BigDecimal("100.00"));

        // Mock exchange rate
        Mockito.when(swopExchangeRateService.getExchangeRate(Currency.EUR, Currency.USD))
                .thenReturn(new BigDecimal("1.05")); // 1 EUR = 1.05 USD

        BigDecimal expectedResponse = new BigDecimal("105.00");

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse.toString()));
    }

    @Test
    void validRequestNoCsrfTokenForbidden() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.EUR);
        request.setTargetCurrency(Currency.USD);
        request.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidAmountBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.BDT);
        request.setTargetCurrency(Currency.TOP);
        request.setAmount(new BigDecimal("100.123"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeAmountBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.USD);
        request.setTargetCurrency(Currency.EUR);
        request.setAmount(new BigDecimal("-100.00"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exceedingMaximumAmountBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.USD);
        request.setTargetCurrency(Currency.EUR);
        request.setAmount(new BigDecimal("1000001"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sameSourceAndTargetCurrencyBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.USD);
        request.setTargetCurrency(Currency.USD);
        request.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidSourceCurrencyBadRequest() throws Exception {
        String invalidRequest = "{ \"sourceCurrency\": \"XYZ\", \"targetCurrency\": \"EUR\", \"amount\": 100.00 }";

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidTargetCurrencyBadRequest() throws Exception {
        String invalidRequest = "{ \"sourceCurrency\": \"USD\", \"targetCurrency\": \"FOO\", \"amount\": 100.00 }";

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void amountStringBadRequest() throws Exception {
        String invalidRequest = "{ \"sourceCurrency\": \"USD\", \"targetCurrency\": \"EUR\", \"amount\": \"BAR\" }";

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void zeroAmountBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.USD);
        request.setTargetCurrency(Currency.EUR);
        request.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sourceCurrencyNullBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(null);
        request.setTargetCurrency(Currency.USD);
        request.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void targetCurrencyNullBadRequest() throws Exception {
        ConversionDto request = new ConversionDto();
        request.setSourceCurrency(Currency.USD);
        request.setTargetCurrency(null);
        request.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
