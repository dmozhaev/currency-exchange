package com.example.demo.controller;

import com.example.demo.dto.ConversionDto;
import com.example.demo.service.SwopExchangeRateService;
import com.example.demo.util.ConversionUtil;
import com.example.demo.validator.ConversionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/convert")
public class ConversionController {

    @Autowired
    private SwopExchangeRateService swopExchangeRateService;

    @PostMapping("/")
    public BigDecimal convertCurrency(@RequestBody ConversionDto dto) throws Exception {
        ConversionValidator.validateConversionRequest(dto);
        BigDecimal exchangeRate = swopExchangeRateService.getExchangeRate(dto.getSourceCurrency(), dto.getTargetCurrency());
        return ConversionUtil.convertToTargetCurrency(dto.getAmount(), exchangeRate);
    }
}
