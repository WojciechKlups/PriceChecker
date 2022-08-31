/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 13:34:49 +0200 (27 sie 2022)
 */
@AllArgsConstructor
@Getter
public enum ProductPageEnum
{
    PSU("https://www.ceneo.pl/51731843", "B"),
    CHASSIS_FAN("https://www.ceneo.pl/42364006", "C"),
    THERMAL_PASTE("https://www.ceneo.pl/39314848", "D"),
    HDD("https://www.ceneo.pl/66746140", "E"),
    RAM("https://www.ceneo.pl/96219325", "F"), //3600
    CHASSIS("https://www.ceneo.pl/93925243", "G"),
    CPU("https://www.ceneo.pl/119352415", "H"),
    MOBO("https://www.ceneo.pl/117535168", "I"),
    SYSTEM_SSD("https://www.ceneo.pl/119770352", "J"),
    DATA_SSD("https://www.ceneo.pl/120380043", "K"),
    CPU_COOLER("https://www.ceneo.pl/120786545", "L"),
    GPU("https://www.ceneo.pl/114861667", "M"),
    GPU_1("https://www.ceneo.pl/112032819", "N"), //5-8%
    GPU_2("https://www.ceneo.pl/96957937", "O"); //25-30%

    private final String url;
    private final String column;
}
