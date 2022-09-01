/***********************************************************
 *
 * Copyright (c) 2022 Wojciech Klupś
 *
 * All rights reserved
 *
 ************************************************************/
package org.wojciechklups.google;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
@Service
public class SheetsService
{
    @Value("${sheet.name}")
    private String sheetName;

    @Value("${sheet.first}")
    private Boolean isFirstSheetFlag;

    private static String SHEET_NAME;

    private static Boolean IS_FIRST_SHEET_FLAG;

    private static String RANGE_SUFFIX;

    private static Sheets sheetsService;

    @PostConstruct
    public void init()
    {
        SHEET_NAME = this.sheetName;
        IS_FIRST_SHEET_FLAG = this.isFirstSheetFlag;
        RANGE_SUFFIX = IS_FIRST_SHEET_FLAG ? "" : String.format("%s!", SHEET_NAME);
    }

    public static void setup() throws GeneralSecurityException, IOException
    {
        sheetsService = SheetsServicePreparer.getSheetsService();
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
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, SHEET_NAME).execute();
        int size = sheet1.getValues().size();
        ValueRange lastPrice = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, column + size).execute();

        return Double.parseDouble(lastPrice.getValues().get(0).get(0).toString());
    }

    public static void writePrices(List<Double> prices) throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, SHEET_NAME).execute();
        int size = sheet1.getValues().size();
        int nextFreeRow = size + 1;

        List<ValueRange> data = new ArrayList<>();

        //Add date
        data.add(new ValueRange()
                .setRange(String.format("%sA%s", RANGE_SUFFIX, nextFreeRow))
                .setValues(Arrays.asList(
                        Arrays.asList(LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                ))
        );

        //Add prices
        data.add(new ValueRange()
                .setRange(String.format("%sB%s", RANGE_SUFFIX, nextFreeRow))
                .setValues(Arrays.asList(
                        Arrays.asList(prices.toArray())))
                );

        //Sum prices with GPU
        data.add(new ValueRange()
                .setRange(String.format("%sP%s", RANGE_SUFFIX, nextFreeRow))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%sM%s)", RANGE_SUFFIX, nextFreeRow, RANGE_SUFFIX, nextFreeRow)))));

        //Sum prices with GPU_1
        data.add(new ValueRange()
                .setRange(String.format("%sQ%s", RANGE_SUFFIX, nextFreeRow))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%SL%s; %sN%s)", RANGE_SUFFIX, nextFreeRow, RANGE_SUFFIX, nextFreeRow, RANGE_SUFFIX, nextFreeRow))))
        );

        //Sum prices with GPU_2
        data.add(new ValueRange()
                .setRange(String.format("%sR%s", RANGE_SUFFIX, nextFreeRow))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%sL%s; %sO%s)", RANGE_SUFFIX, nextFreeRow, RANGE_SUFFIX, nextFreeRow, RANGE_SUFFIX, nextFreeRow))))
        );

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(SPREADSHEET_ID, batchBody)
                .execute();
    }
}
