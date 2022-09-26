/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.service.ceneoservices;

import org.wojciechklups.enums.ProductPageEnum;
import org.wojciechklups.service.ceneoservices.RequestSenderService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.wojciechklups.WebClientPreparer.webClient;

/**
 * This class handles preparing prices from ceneo page response.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 21:12:38 +0200 (27 sie 2022)
 */
public class ResponsePreparer
{
    RequestSenderService requestSenderService = new RequestSenderService(webClient());

    public List<Double> getPreparedResponses()
    {
        return Arrays.stream(ProductPageEnum.values()).parallel()
                .map(productPage -> Double.parseDouble(requestSenderService.getCeneoPage(productPage)
                        .split("\"lowPrice\": ")[1].split(",")[0]))
                .collect(Collectors.toList());
    }
}
