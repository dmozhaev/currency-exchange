package com.example.demo.controller;

import com.example.demo.dto.ConversionDto;
import com.example.demo.service.SwopExchangeRateService;
import com.example.demo.util.ConversionUtil;
import com.example.demo.validator.ConversionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/convert")
public class ConversionController {
    private static final Logger logger = LoggerFactory.getLogger(ConversionController.class);

    @Autowired
    private SwopExchangeRateService swopExchangeRateService;

    @PostMapping("/")
    public BigDecimal convertCurrency(@RequestBody ConversionDto dto) throws IllegalArgumentException, IllegalStateException, IOException, InterruptedException  {
        logger.debug("ConversionController: Request to convert {} {} to {}", dto.getAmount(), dto.getSourceCurrency(), dto.getTargetCurrency());

        try {
            ConversionValidator.validateConversionRequest(dto);
            BigDecimal exchangeRate = swopExchangeRateService.getExchangeRate(dto.getSourceCurrency(), dto.getTargetCurrency());
            BigDecimal result = ConversionUtil.convertToTargetCurrency(dto.getAmount(), exchangeRate);

            logger.debug("ConversionController: Conversion successful, result: {} {}", result, dto.getTargetCurrency());

            return result;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (SecurityException e) {
            logger.error(e.getMessage());
            throw new SecurityException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
            throw new IllegalStateException(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new IOException(e);
        }
    }
}
