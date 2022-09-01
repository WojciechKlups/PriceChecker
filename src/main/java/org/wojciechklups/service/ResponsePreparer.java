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
import java.util.List;

import static org.wojciechklups.WebClientPreparer.webClient;

/**
 * To change active spring profile please go into Project settings -> VM environment -> and paste -Dspring.profiles.active={profil_name}
 * Profiles: test, dev
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 21:12:38 +0200 (27 sie 2022)
 */
public class ResponsePreparer
{
    RequestSenderService requestSenderService = new RequestSenderService(webClient());

    public List<Double> getPreparedResponses()
    {
        List<Double> results = new ArrayList<>();

        for (ProductPageEnum productEnum : ProductPageEnum.values())
        {
            results.add(Double.parseDouble(requestSenderService.getCeneoPage(productEnum)
                    .split("\"lowPrice\": ")[1].split(",")[0]));
        }

        return results;
    }
}
