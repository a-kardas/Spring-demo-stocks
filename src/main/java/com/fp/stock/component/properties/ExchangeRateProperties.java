package com.fp.stock.component.properties;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "exchange")
public class ExchangeRateProperties {

    @NotBlank
    private String url;

    @NotBlank
    private String port;

    @NotBlank
    private String accessPath;
}
