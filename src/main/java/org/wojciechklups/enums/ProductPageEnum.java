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
 *  This enum contains links to ceneo pages of given PC components.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-27 13:34:49 +0200 (27 sie 2022)
 */
@AllArgsConstructor
@Getter
public enum ProductPageEnum
{
    DATE("", "A"),
    PSU("https://www.ceneo.pl/51731843", "B"),
    CHASSIS_FAN("https://www.ceneo.pl/42364006", "C"),
    THERMAL_PASTE("https://www.ceneo.pl/39314848", "D"),
    HDD("https://www.ceneo.pl/66746140", "E"),
    RAM("https://www.ceneo.pl/111801433", "F"),
    CHASSIS("https://www.ceneo.pl/93925243", "G"),
    CPU("https://www.ceneo.pl/140261842", "H"),
    MOBO("https://www.ceneo.pl/117461531", "I"),
    SYSTEM_SSD("https://www.ceneo.pl/119770352", "J"),
    DATA_SSD("https://www.ceneo.pl/120380043", "K"),
    CPU_COOLER("https://www.ceneo.pl/120786545", "L"),
    GPU("https://www.ceneo.pl/98578185", "M"), //zostajemy tutaj bo celujemy w 1440p, monitor 27 cali
    GPU_1("https://www.ceneo.pl/112032819", "N"), //5-8%
    GPU_2("https://www.ceneo.pl/113390266", "O"), //25-30%
    TOTAL("", "P"),
    TOTAL_1("", "Q"),
    TOTAL_2("", "R");

    private final String url;
    private final String column;
}
