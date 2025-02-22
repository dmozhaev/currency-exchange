package com.example.demo.controller;

import com.example.demo.dto.ConversionDto;
import com.example.demo.service.SwopExchangeRateService;
import com.example.demo.util.ConversionUtil;
import com.example.demo.validator.ConversionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/convert")
public class ConversionController {
    private static final Logger logger = LoggerFactory.getLogger(ConversionController.class);

    @Autowired
    private SwopExchangeRateService swopExchangeRateService;

    @PostMapping("/")
    public BigDecimal convertCurrency(@RequestBody ConversionDto dto) throws Exception {
        logger.debug("ConversionController: Request to convert {} {} to {}", dto.getAmount(), dto.getSourceCurrency(), dto.getTargetCurrency());

        ConversionValidator.validateConversionRequest(dto);
        BigDecimal exchangeRate = swopExchangeRateService.getExchangeRate(dto.getSourceCurrency(), dto.getTargetCurrency());
        BigDecimal result = ConversionUtil.convertToTargetCurrency(dto.getAmount(), exchangeRate);

        logger.debug("ConversionController: Conversion successful, result: {} {}", result, dto.getTargetCurrency());

        return result;
    }
}
