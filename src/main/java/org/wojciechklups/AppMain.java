package org.wojciechklups;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.wojciechklups.enums.ProductPageEnum;
import org.wojciechklups.service.RequestSenderService;

@SpringBootApplication
@AllArgsConstructor
public class AppMain extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        RequestSenderService requestSenderService = new RequestSenderService(webClient());
        System.out.println(requestSenderService.getCeneoPage(ProductPageEnum.CPU).split("\"lowPrice\": ")[1].split(",")[0]);
    }

    @Bean
    public static WebClient webClient() {
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
