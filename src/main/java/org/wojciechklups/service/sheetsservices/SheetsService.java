/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.service.sheetsservices;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import org.wojciechklups.google.DriveServicePreparer;
import org.wojciechklups.google.SheetsServicePreparer;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.wojciechklups.google.SheetsServicePreparer.SPREADSHEET_ID;

/**
 * This service handles all necessary methods that are used to read/write in google sheets.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:14:21 +0200 (29 sie 2022)
 */
@Slf4j
public class SheetsService
{
    private static Sheets sheetsService;
    private static Drive driveService;

    public static void setup() throws GeneralSecurityException, IOException
    {
        driveService = DriveServicePreparer.getDriveService();
        sheetsService = SheetsServicePreparer.getSheetsService();

        FileList searchResult = driveService.files().list()
                .setQ("name=Price Checker Sheet")
                .setSpaces("drive")
                .execute();

        if (searchResult.isEmpty())
        {
            log.info("App didn't found file named 'Price Checker Sheet'. The app will create one and will perform the rest of operations on it.");
            //stwórz takiego sheeta
        }
        else
        {
            log.info("Found file named 'Price Checker Sheet'. App now will operate on that file.");
        }
    }

//    public String getFirstFreeColumnCell() throws IOException
//    {
//        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
//        List<DimensionGroup> columnGroups = spreadsheet.getSheets().get(0).getColumnGroups();
//    }

    public void colourPrices(List<Double> lastPrices)
    {

    }

    public static Double readLastPrice(String column) throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, "Arkusz1").execute();
        int size = sheet1.getValues().size();

        ValueRange lastPrice = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, column + size).execute();
        return Double.parseDouble(lastPrice.getValues().get(0).get(0).toString());
    }

    public static void writePrices(List<Double> prices) throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, "Arkusz1").execute();
        int size = sheet1.getValues().size();
        int nextFreeRow = size + 1;

        List<ValueRange> data = new ArrayList<>();

        //Add date
        data.add(new ValueRange()
                .setRange("A" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList(LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                ))
        );

        //Add prices
        data.add(new ValueRange()
                .setRange("B" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList(prices.toArray())))
                );

        //Sum prices with GPU
        data.add(new ValueRange()
                .setRange("P" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList("=SUMA(B" + nextFreeRow + ":M" + nextFreeRow + ")"))));

        //Sum prices with GPU_1
        data.add(new ValueRange()
                .setRange("Q" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList("=SUMA(B" + nextFreeRow + ":L" + nextFreeRow + "; N" + nextFreeRow + ")")))
        );

        //Sum prices with GPU_2
        data.add(new ValueRange()
                .setRange("R" + nextFreeRow)
                .setValues(Arrays.asList(
                        Arrays.asList("=SUMA(B" + nextFreeRow + ":L" + nextFreeRow + "; O" + nextFreeRow + ")")))
        );

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(SPREADSHEET_ID, batchBody)
                .execute();
    }
}
