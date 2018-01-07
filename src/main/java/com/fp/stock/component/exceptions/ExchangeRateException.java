package com.fp.stock.component.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateException extends Exception {

    private boolean isCritical;

    private String message;
}
