package com.example.demo.validator;

import com.example.demo.dto.ConversionDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConversionValidator {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000");

    public static void validateConversionRequest(ConversionDto dto) throws Exception {
        List<String> errors = new ArrayList<>();

        // Validate source currency
        if (dto.getSourceCurrency() == null) {
            errors.add("Source currency must not be null.");
        }

        // Validate target currency
        if (dto.getTargetCurrency() == null) {
            errors.add("Target currency must not be null.");
        }

        // Ensure source and target currencies should be different
        if (dto.getSourceCurrency() != null && dto.getSourceCurrency().equals(dto.getTargetCurrency())) {
            errors.add("Source and target currencies must be different.");
        }

        // Validate amount (non-null, positive and cents check)
        if (dto.getAmount() == null) {
            errors.add("Amount must not be null.");
        } else {
            if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Amount must be greater than zero.");
            }
            if (dto.getAmount().scale() > 2) {
                errors.add("Amount must have at most two decimal places.");
            }
            if (dto.getAmount().compareTo(MAX_AMOUNT) > 0) {
                errors.add("Amount must not exceed 1,000,000.");
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception(String.join(", ", errors));
        }
    }
}
