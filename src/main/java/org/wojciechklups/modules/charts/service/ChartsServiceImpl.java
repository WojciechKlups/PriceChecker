package org.wojciechklups.modules.charts.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wojciechklups.google.SheetsServicePreparer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Author: psobieraj
 * @timestamp Date: 2022-10-23 20:44:19 +0200 (23 paź 2022)
 */
@Service
public class ChartsServiceImpl
{
    @Value("${sheet.name}")
    private String sheetName;

    @Value("${sheet.isFirst}")
    private Boolean isFirstSheetFlag;

    @Value("${sheet.gid}")
    private Integer sheetGid;

    private String rangeSuffix;

    private final String SPREADSHEET_ID =  "1iNzLpyVqGmnHeyOU-VkjsqhUca2g7RPdtMFJNLUPQaU"; // TO DO - moving this variable maybe to the yaml with config

    private Sheets sheetsService;

    @PostConstruct
    public void init() throws GeneralSecurityException, IOException
    {
        this.rangeSuffix = isFirstSheetFlag ? "" : String.format("%s!", sheetName);
        this.sheetsService = SheetsServicePreparer.getSheetsService();
    }

    public void addTestChart() throws IOException
    {
        List<Request> requests = new ArrayList<>();

        Integer chartStartRow = sheetsService // Chart start row = all rows + 1
                .spreadsheets()
                .values()
                .get(SPREADSHEET_ID, sheetName)
                .execute()
                .getValues()
                .size() + 1;

        requests.add(new Request().setAddChart(new AddChartRequest().setChart(new EmbeddedChart()
                .setSpec(new ChartSpec()
                        .setTitle("Computer parts prices")
                        .setBasicChart(new BasicChartSpec()
                                .setChartType("COLUMN")
                                .setLegendPosition("BOTTOM_LEGEND")
                                .setAxis(new ArrayList<>(){{
                                    add(new BasicChartAxis()
                                            .setPosition("BOTTOM_AXIS")
                                            .setTitle("Date")); // Date
                                    add(new BasicChartAxis()
                                            .setPosition("LEFT_AXIS")
                                            .setTitle("Price"));  // Price
                                }})
                                .setDomains(new ArrayList<>(){{
                                    add(new BasicChartDomain()
                                            .setDomain(new ChartData()
                                                    .setSourceRange(new ChartSourceRange()
                                                            .setSources(Arrays.asList(
                                                                    new GridRange()
                                                                            .setSheetId(sheetGid)
                                                                            .setStartRowIndex(1)
                                                                            .setEndRowIndex(7)
                                                                            .setStartColumnIndex(0)
                                                                            .setEndColumnIndex(1)
                                                            )))));
                                }})
                                .setSeries(Arrays.asList(
                                        new BasicChartSeries()
                                                .setSeries(new ChartData()
                                                        .setSourceRange(new ChartSourceRange()
                                                                .setSources(Arrays.asList(
                                                                        new GridRange()
                                                                                .setSheetId(sheetGid)
                                                                                .setStartRowIndex(1)
                                                                                .setEndRowIndex(7)
                                                                                .setStartColumnIndex(1)
                                                                                .setEndColumnIndex(2)
                                                                ))))
                                                .setTargetAxis("LEFT_AXIS"),
                                        new BasicChartSeries()
                                                .setSeries(new ChartData()
                                                        .setSourceRange(new ChartSourceRange()
                                                                .setSources(Arrays.asList(
                                                                        new GridRange()
                                                                                .setSheetId(sheetGid)
                                                                                .setStartRowIndex(1)
                                                                                .setEndRowIndex(7)
                                                                                .setStartColumnIndex(2)
                                                                                .setEndColumnIndex(3)
                                                                ))))
                                                .setTargetAxis("LEFT_AXIS"),
                                        new BasicChartSeries()
                                                .setSeries(new ChartData()
                                                        .setSourceRange(new ChartSourceRange()
                                                                .setSources(Arrays.asList(
                                                                        new GridRange()
                                                                                .setSheetId(sheetGid)
                                                                                .setStartRowIndex(1)
                                                                                .setEndRowIndex(7)
                                                                                .setStartColumnIndex(3)
                                                                                .setEndColumnIndex(4)
                                                                ))))
                                                .setTargetAxis("LEFT_AXIS")
                                ))))
                .setPosition(new EmbeddedObjectPosition()
                        .setOverlayPosition(new OverlayPosition()
                                .setAnchorCell(new GridCoordinate()
                                        .setSheetId(sheetGid)
                                        .setRowIndex(chartStartRow)
                                        .setColumnIndex(0))))))
        );

        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);

        Sheets.Spreadsheets.BatchUpdate request =
                sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, requestBody);

        BatchUpdateSpreadsheetResponse response = request.execute();
        System.out.println(response);
    }

    private ArrayList<BasicChartDomain> prepareDomain(Integer startRowIndex, Integer endRowIndex, Integer startColumnIndex, Integer endColumnIndex)
    {
        return new ArrayList<>(){{
            add(new BasicChartDomain()
                    .setDomain(new ChartData()
                            .setSourceRange(new ChartSourceRange()
                                    .setSources(Arrays.asList(
                                            new GridRange()
                                                    .setSheetId(sheetGid)
                                                    .setStartRowIndex(startRowIndex) // 1
                                                    .setEndRowIndex(endRowIndex) // 7
                                                    .setStartColumnIndex(startColumnIndex) // 0
                                                    .setEndColumnIndex(endColumnIndex) // 1
                                    )))));
        }};
    }

    private BasicChartSeries prepareSingleSeries(Integer startRowIndex, Integer endRowIndex, Integer startColumnIndex, Integer endColumnIndex)
    {
        return new BasicChartSeries()
                .setSeries(new ChartData()
                        .setSourceRange(new ChartSourceRange()
                                .setSources(Arrays.asList(
                                        new GridRange()
                                                .setSheetId(sheetGid)
                                                .setStartRowIndex(1) // 1
                                                .setEndRowIndex(7) // 7
                                                .setStartColumnIndex(2) // 1,2,3
                                                .setEndColumnIndex(3) // 2,3,4
                                ))))
                .setTargetAxis("LEFT_AXIS");
    }

    private EmbeddedObjectPosition prepareChartPosition(Integer chartStartRow, Integer columnIndex)
    {
        return new EmbeddedObjectPosition()
                .setOverlayPosition(new OverlayPosition()
                        .setAnchorCell(new GridCoordinate()
                                .setSheetId(sheetGid)
                                .setRowIndex(chartStartRow)
                                .setColumnIndex(columnIndex)));
    }

    private BatchUpdateSpreadsheetResponse addChart(List<Request> requests) throws IOException
    {
        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        Sheets.Spreadsheets.BatchUpdate request = sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, requestBody);

        return request.execute();
    }

    private Integer calculateStarRowIndex(Integer startIndexOffset) throws IOException
    {
        return  sheetsService // Chart start row = all rows + 1
                .spreadsheets()
                .values()
                .get(SPREADSHEET_ID, sheetName)
                .execute()
                .getValues()
                .size() + startIndexOffset; // 1
    }

    private List<Request> prepareChart(String chartName, String bottomAxisName, String leftAxisName,  ArrayList<BasicChartDomain> chartDomain, ArrayList<BasicChartSeries> chartSeriesList, EmbeddedObjectPosition chartPosition) throws IOException
    {
        return new ArrayList<Request>()
        {{
            add(new Request().setAddChart(new AddChartRequest().setChart(new EmbeddedChart()
                .setSpec(new ChartSpec()
                        .setTitle(chartName) // "Computer parts prices"
                        .setBasicChart(new BasicChartSpec()
                                .setChartType("COLUMN")
                                .setLegendPosition("BOTTOM_LEGEND")
                                .setAxis(new ArrayList<>()
                                {{
                                    add(new BasicChartAxis()
                                            .setPosition("BOTTOM_AXIS")
                                            .setTitle(bottomAxisName)); // Date
                                    add(new BasicChartAxis()
                                            .setPosition("LEFT_AXIS")
                                            .setTitle(leftAxisName));  // Price
                                }})
                                .setDomains(chartDomain)
                                .setSeries(chartSeriesList)))
                .setPosition(chartPosition))));
        }};
    }
}
