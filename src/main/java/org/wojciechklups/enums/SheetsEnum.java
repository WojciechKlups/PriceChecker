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
 * @timestamp Date: 2022-11-25 20:12:53 +0200 (25 lis 2022)
 */
@AllArgsConstructor
@Getter
public enum SheetsEnum
{
    CREDENTIALS_FILE_PATH ("/google-sheets-client-secret.json"),
    TOKENS_DIRECTORY_PATH ("/tokens"),
    APPLICATION_NAME ("Price Checker");

    private final String value;
}
