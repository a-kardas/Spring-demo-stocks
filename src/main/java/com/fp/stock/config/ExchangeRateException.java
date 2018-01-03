package com.fp.stock.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateException extends Exception {

    private boolean isCritical;

    private String message;
}
