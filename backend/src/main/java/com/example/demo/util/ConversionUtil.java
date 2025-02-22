package com.example.demo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConversionUtil {
    public static BigDecimal convertToTargetCurrency(BigDecimal amount, BigDecimal exchangeRate) {
        BigDecimal result = amount.multiply(exchangeRate);
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
