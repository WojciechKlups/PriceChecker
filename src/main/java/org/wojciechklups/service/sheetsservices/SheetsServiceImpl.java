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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.wojciechklups.enums.ProductPageEnum;
import org.wojciechklups.google.DriveServicePreparer;
import org.wojciechklups.google.SheetsServicePreparer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This service handles all necessary methods that are used to read/write in google sheets.
 *
 * @author Author: wklups
 * @timestamp Date: 2022-08-29 15:14:21 +0200 (29 sie 2022)
 */
@Service
@Slf4j
public class SheetsServiceImpl implements SheetService
{
    private String sheetName = "";

    private final Boolean isFirstSheetFlag;

    private String currentSheetName;

    private String rangeSuffix;
    public String spreadsheetId = "";

    private final Sheets sheetsService;
    private final Drive driveService;

    @Autowired
    private Environment environment;

    public SheetsServiceImpl() throws GeneralSecurityException, IOException
    {
        SheetsServicePreparer sheetsServicePreparer = new SheetsServicePreparer();
        DriveServicePreparer driveServicePreparer = new DriveServicePreparer();

        this.sheetsService = sheetsServicePreparer.getSheetsService();
        this.driveService = driveServicePreparer.getDriveService();

        this.sheetName = environment.getProperty("sheet.name");
        this.isFirstSheetFlag = Boolean.parseBoolean(environment.getProperty("sheet.isFirst"));
    }

//    @PostConstruct
//    public void init()
//    {
//        currentSheetName = this.sheetName;
//        Boolean isFirstSheetFlag = this.isFirstSheetFlag;
//        this.rangeSuffix = isFirstSheetFlag ? "" : String.format("%s!", currentSheetName);
//    }

    public void setup() throws IOException
    {
        getSpreadsheetOrCreateNew();
    }

    private void getSpreadsheetOrCreateNew() throws IOException
    {
        String userEmail = driveService.about().get().setFields("user").execute().getUser().getEmailAddress();

        FileList searchResult = driveService.files().list()
                .setQ("name='Price Checker Sheet' and '" + userEmail + "' in owners")
                .setSpaces("drive")
                .execute();

        if (searchResult.getFiles().isEmpty())
        {
            log.info("App didn't found file named 'Price Checker Sheet'. The app will create one and will perform the rest of operations on it.");

            Spreadsheet spreadsheet = new Spreadsheet()
                    .setProperties(new SpreadsheetProperties().setTitle("Price Checker Sheet"));

            spreadsheet = sheetsService.spreadsheets().create(spreadsheet)
                    .setFields("spreadsheetId")
                    .execute();

            spreadsheetId = spreadsheet.getSpreadsheetId();

            createHeadersInSpreadsheet();
        }
        else
        {
            log.info("Found file named 'Price Checker Sheet'. App now will operate on that file.");

            spreadsheetId = searchResult.getFiles().get(0).getId();
        }
    }

    private void createHeadersInSpreadsheet() throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(spreadsheetId, currentSheetName).execute();
        List<ValueRange> data = new ArrayList<>();

        data.add(new ValueRange() // To fix
                .setRange(String.format("%s%s1:%s%s", rangeSuffix,
                        ProductPageEnum.DATE.getColumn(),
                        ProductPageEnum.TOTAL_2.getColumn(),
                        ProductPageEnum.values().length))
                .setValues(Arrays.asList(Arrays.stream(ProductPageEnum.values()).map(Enum::toString).collect(Collectors.toList()
                )))
        );

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(spreadsheetId, batchBody)
                .execute();
    }

    public void colourPrices(List<Double> lastPrices)
    {

    }

    public String readLastPrice(String column) throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(spreadsheetId, currentSheetName).execute();
        int size = sheet1.getValues().size();
        ValueRange lastPrice = sheetsService.spreadsheets().values().get(spreadsheetId, rangeSuffix + column + size).execute();

        return lastPrice.getValues().get(0).get(0).toString();
    }

    public void writePrices(List<Double> prices) throws IOException
    {
        log.info("Writing prices START");

        List<ValueRange> data = new ArrayList<>();

        addDate(data);

        addPrices(data, prices);

        sumPrices(data);

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(spreadsheetId, batchBody)
                .execute();

        log.info("Writing prices END");
    }

    private int getNextFreeRow() throws IOException
    {
        ValueRange sheet1 = sheetsService.spreadsheets().values().get(spreadsheetId, currentSheetName).execute();
        int size = sheet1.getValues().size();
        return size + 1;
    }

    private void addDate(List<ValueRange> data) throws IOException
    {
        data.add(new ValueRange()
                .setRange(String.format("%sA%s", rangeSuffix, getNextFreeRow()))
                .setValues(Arrays.asList(
                        Arrays.asList(LocalDate.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                ))
        );
    }

    private void addPrices(List<ValueRange> data, List<Double> prices) throws IOException
    {
        data.add(new ValueRange()
                .setRange(String.format("%sB%s", rangeSuffix, getNextFreeRow()))
                .setValues(Arrays.asList(
                        Arrays.asList(prices.toArray())))
        );
    }

    private void sumPrices(List<ValueRange> data) throws IOException
    {
        //Sum prices with GPU
        data.add(new ValueRange()
                .setRange(String.format("%sP%s", rangeSuffix, getNextFreeRow()))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%sM%s)", rangeSuffix, getNextFreeRow(),
                                rangeSuffix, getNextFreeRow())))));

        //Sum prices with GPU_1
        data.add(new ValueRange()
                .setRange(String.format("%sQ%s", rangeSuffix, getNextFreeRow()))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%SL%s; %sN%s)", rangeSuffix, getNextFreeRow(),
                                rangeSuffix, getNextFreeRow(), rangeSuffix, getNextFreeRow()))))
        );

        //Sum prices with GPU_2
        data.add(new ValueRange()
                .setRange(String.format("%sR%s", rangeSuffix, getNextFreeRow()))
                .setValues(Arrays.asList(
                        Arrays.asList(String.format("=SUMA(%sB%s:%sL%s; %sO%s)", rangeSuffix, getNextFreeRow(),
                                rangeSuffix, getNextFreeRow(), rangeSuffix, getNextFreeRow()))))
        );
    }
}
