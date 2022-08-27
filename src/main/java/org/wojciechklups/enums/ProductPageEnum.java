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
    PSU("https://www.ceneo.pl/51731843"),
    CHASSIS_FAN("https://www.ceneo.pl/42364006"),
    THERMAL_PASTE("https://www.ceneo.pl/39314848"),
    HDD("https://www.ceneo.pl/66746140"),
    RAM("https://www.ceneo.pl/55525714"),
    CHASSIS("https://www.ceneo.pl/93925243"),
    CPU("https://www.ceneo.pl/119352415"),
    MOBO("https://www.ceneo.pl/117535168"),
    SYSTEM_SSD("https://www.ceneo.pl/119770352"),
    DATA_SSD("https://www.ceneo.pl/120380043"),
    CPU_COOLER("https://www.ceneo.pl/120786545"),
    GPU("https://www.ceneo.pl/114861667");

    private final String url;
}
