package com.example.demo.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.ConversionDto;
import com.example.demo.enums.Currency;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class ConversionValidatorTest {

    @Test
    void validAmountAndCurrencies() {
        ConversionDto dto1 = createDto(Currency.USD, Currency.EUR, new BigDecimal("10.00"));
        ConversionDto dto2 = createDto(Currency.GBP, Currency.JPY, new BigDecimal("99.99"));
        ConversionDto dto3 = createDto(Currency.ZMW, Currency.UAH, new BigDecimal("12345.67"));
        assertDoesNotThrow(() -> ConversionValidator.validateConversionRequest(dto1));
        assertDoesNotThrow(() -> ConversionValidator.validateConversionRequest(dto2));
        assertDoesNotThrow(() -> ConversionValidator.validateConversionRequest(dto3));
    }

    @Test
    void tooBigAmount() {
        ConversionDto dto1 = createDto(Currency.TOP, Currency.UZS, new BigDecimal("1000000"));
        ConversionDto dto2 = createDto(Currency.IRR, Currency.ETB, new BigDecimal("1000001"));
        assertDoesNotThrow(() -> ConversionValidator.validateConversionRequest(dto1));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto2));
        assertTrue(exception.getMessage().contains("Amount must not exceed 1,000,000."));
    }

    @Test
    void tooSmallAmount() {
        ConversionDto dto1 = createDto(Currency.TOP, Currency.UZS, new BigDecimal("0.01"));
        ConversionDto dto2 = createDto(Currency.IRR, Currency.ETB, new BigDecimal("0.009"));
        assertDoesNotThrow(() -> ConversionValidator.validateConversionRequest(dto1));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto2));
        assertTrue(exception.getMessage().contains("Amount must have at most two decimal places."));
    }

    @Test
    void amountWithMoreThanTwoDecimals() {
        ConversionDto dto = createDto(Currency.USD, Currency.EUR, new BigDecimal("100.123"));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto));
        assertTrue(exception.getMessage().contains("Amount must have at most two decimal places."));
    }

    @Test
    void negativeAmount() {
        ConversionDto dto = createDto(Currency.USD, Currency.EUR, new BigDecimal("-5.50"));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto));
        assertTrue(exception.getMessage().contains("Amount must be greater than zero."));
    }

    @Test
    void nullAmount() {
        ConversionDto dto = createDto(Currency.USD, Currency.EUR, null);
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto));
        assertTrue(exception.getMessage().contains("Amount must not be null."));
    }

    @Test
    void nullCurrencies() {
        ConversionDto dto = createDto(null, null, new BigDecimal("10.00"));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto));
        assertTrue(exception.getMessage().contains("Source currency must not be null."));
        assertTrue(exception.getMessage().contains("Target currency must not be null."));
    }

    @Test
    void sameSourceAndTargetCurrency() {
        ConversionDto dto = createDto(Currency.USD, Currency.USD, new BigDecimal("10.00"));
        Exception exception =
                assertThrows(
                        Exception.class, () -> ConversionValidator.validateConversionRequest(dto));
        assertTrue(
                exception.getMessage().contains("Source and target currencies must be different."));
    }

    private ConversionDto createDto(Currency source, Currency target, BigDecimal amount) {
        ConversionDto dto = new ConversionDto();
        dto.setSourceCurrency(source);
        dto.setTargetCurrency(target);
        dto.setAmount(amount);
        return dto;
    }
}
