/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:14:21 +0200 (29 sie 2022)
 */
public class SheetsService
{
    private static Sheets sheetsService;
    private static final String SPREADSHEET_ID = "1lPzVIsAs_hNmO-2AvKmovfOHSIE_06fyOpK7LTvqWLA";

    public static void setup() throws GeneralSecurityException, IOException
    {
        sheetsService = SheetsServicePreparer.getSheetsService();
    }

    public static void writeSomething() throws IOException
    {
            ValueRange body = new ValueRange()
                    .setValues(Arrays.asList(
                            Arrays.asList("Expenses January"),
                            Arrays.asList("books", "30"),
                            Arrays.asList("pens", "10"),
                            Arrays.asList("Expenses February"),
                            Arrays.asList("clothes", "20"),
                            Arrays.asList("shoes", "5")));

            UpdateValuesResponse result = sheetsService.spreadsheets().values()
                    .update(SPREADSHEET_ID, "A1", body)
                    .setValueInputOption("RAW")
                    .execute();
    }
}
