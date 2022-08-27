/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.service;

import org.wojciechklups.enums.ProductPageEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.wojciechklups.WebClientPreparer.webClient;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 21:12:38 +0200 (27 sie 2022)
 */
public class ResponsePreparer
{
    RequestSenderService requestSenderService = new RequestSenderService(webClient());

    public List<String> getPreparedResponses()
    {
        List<String> results = new ArrayList<>();

        for (ProductPageEnum productEnum : ProductPageEnum.values())
        {
            results.add(productEnum.name() + "-" + requestSenderService.getCeneoPage(productEnum)
                    .split("\"lowPrice\": ")[1].split(",")[0]);
        }

        return results;
    }

}
