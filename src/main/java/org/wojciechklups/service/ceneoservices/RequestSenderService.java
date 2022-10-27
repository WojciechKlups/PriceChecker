/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.service.ceneoservices;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.wojciechklups.enums.ProductPageEnum;

/**
 * This class handles getting response from ceneo pages.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 13:49:28 +0200 (27 sie 2022)
 */
@AllArgsConstructor
@Service
public class RequestSenderService
{
    @Autowired
    private WebClient webClient;

    public String getCeneoPage(ProductPageEnum productPage)
    {
        return webClient.get()
                .uri(productPage.getUrl())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
