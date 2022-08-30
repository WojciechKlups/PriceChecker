/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.wojciechklups.google.SheetsServicePreparer.SPREADSHEET_ID;

/**
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:14:21 +0200 (29 sie 2022)
 */
public class SheetsService
{
    private static Sheets sheetsService;

    public static void setup() throws GeneralSecurityException, IOException
    {
        sheetsService = SheetsServicePreparer.getSheetsService();
    }

//    public String getFirstFreeColumnCell() throws IOException
//    {
//        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
//        List<DimensionGroup> columnGroups = spreadsheet.getSheets().get(0).getColumnGroups();
//    }

    public static void writePrices(List<Double> prices) throws IOException
    {

        ValueRange sheet1 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, "Arkusz1").execute();
        int size = sheet1.getValues().size();
        int nextFreeRow = size + 1;

        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange()
                .setRange("A" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList(LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                ))
        );

        data.add(new ValueRange()
                .setRange("B" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList(prices.toArray())))
                );

        data.add(new ValueRange()
                .setRange("N" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList("=SUMA(B" + nextFreeRow + ":M" + nextFreeRow + ")"))));

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(SPREADSHEET_ID, batchBody)
                .execute();

//            ValueRange body = new ValueRange()
//                    .setValues(Arrays.asList(
//                            Arrays.asList(prices.toArray())
//                    ))
//                    .setMajorDimension("COLUMNS");
//
//
//            UpdateValuesResponse result = sheetsService.spreadsheets().values()
//                    .update(SPREADSHEET_ID, "B1", body)
//                    .setValueInputOption("RAW")
//                    .execute();
    }
}
