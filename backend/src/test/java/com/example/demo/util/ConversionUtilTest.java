package com.example.demo.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversionUtilTest {

    @Test
    void validAmountAndRate() {
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal exchangeRate = new BigDecimal("1.10");
        BigDecimal expected = new BigDecimal("110.00"); // 100 * 1.10 = 110.00

        BigDecimal result = ConversionUtil.convertToTargetCurrency(amount, exchangeRate);
        assertEquals(expected, result);
    }

    @Test
    void smallAmountAndRate() {
        BigDecimal amount = new BigDecimal("1.23");
        BigDecimal exchangeRate = new BigDecimal("1.5678");
        BigDecimal expected = new BigDecimal("1.93"); // 1.23 * 1.5678 = 1.928394 -> rounds to 1.93

        BigDecimal result = ConversionUtil.convertToTargetCurrency(amount, exchangeRate);
        assertEquals(expected, result);
    }

    @Test
    void halfUpRoundingShouldRoundDownCorrectly() {
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal exchangeRate = new BigDecimal("0.3333");
        BigDecimal expected = new BigDecimal("3.33"); // 10.00 * 0.3333 = 3.333 -> rounds to 3.33

        BigDecimal result = ConversionUtil.convertToTargetCurrency(amount, exchangeRate);
        assertEquals(expected, result);
    }

    @Test
    void halfUpRoundingShouldRoundUpCorrectly() {
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal exchangeRate = new BigDecimal("0.3755");
        BigDecimal expected = new BigDecimal("3.76"); // 10 * 0.3755 = 3.755 -> rounds to 3.76

        BigDecimal result = ConversionUtil.convertToTargetCurrency(amount, exchangeRate);
        assertEquals(expected, result);
    }

    @Test
    void noRoundingWhenNotNeeded() {
        BigDecimal amount = new BigDecimal("10.00");
        BigDecimal exchangeRate = new BigDecimal("0.375");
        BigDecimal expected = new BigDecimal("3.75"); // 10 * 0.375 = 3.75

        BigDecimal result = ConversionUtil.convertToTargetCurrency(amount, exchangeRate);
        assertEquals(expected, result);
    }
}
