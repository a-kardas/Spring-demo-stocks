package com.fp.stock.component;

import com.fp.stock.component.events.ExchangeRateDownloadedEvent;
import com.fp.stock.component.events.ExchangeRateErrorEvent;
import com.fp.stock.component.events.ExchangeRateSuccessEvent;
import com.fp.stock.config.ExchangeRateException;
import com.fp.stock.config.ExchangeRateProperties;
import com.fp.stock.dto.ExternalStockListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class StockComponent {

    private final Logger log = LoggerFactory.getLogger(StockComponent.class);

    private final String URL_TEMPLATE = "%s:%s/%s";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExchangeRateProperties exchangeRateProperties;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Scheduled(cron = "*/10 * * * * *") //every 10 seconds
    public void downloadExchangeRate() throws ExchangeRateException {
        log.info("Scheduled job - downloading exchange rate");
        String url = String.format(URL_TEMPLATE, exchangeRateProperties.getUrl(),
                                                 exchangeRateProperties.getPort(),
                                                 exchangeRateProperties.getAccessPath());
        try {
            ResponseEntity<ExternalStockListDTO> response = restTemplate.getForEntity(url, ExternalStockListDTO.class);
            ExternalStockListDTO stocks = response.getBody();

            log.debug("Scheduled job - downloaded rates with publication date " + stocks.getPublicationDate());

            this.publisher.publishEvent(new ExchangeRateDownloadedEvent(stocks));
            this.publisher.publishEvent(new ExchangeRateSuccessEvent());

        } catch (RestClientException exception) {
            log.error("Error in scheduled job. Exchange rates can not be downloaded.");
            this.publisher.publishEvent(new ExchangeRateErrorEvent());
            throw new ExchangeRateException(true, "Exchange rates can not be downloaded.");
        }
    }
}
