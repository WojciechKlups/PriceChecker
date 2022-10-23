package org.wojciechklups.modules.charts.model;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.wojciechklups.google.SheetsServicePreparer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Chart model class which contains its own builder static class (used builder design pattern).
 *
 * @author Author: psobieraj
 * @timestamp Date: 2022-10-23 21:52:50 +0200 (23 paź 2022)
 */
@Getter
public class Chart
{
    private List<Request> chart;
    private Integer startRowIndexWithOffset;
    private String chartName;
    private String bottomAxisName;
    private String leftAxisName;
    private EmbeddedObjectPosition chartPosition;
    private ArrayList<BasicChartDomain> chartDomains;
    private ArrayList<BasicChartSeries> chartSeries;

    public Chart(List<Request> chart, Integer startRowIndexWithOffset, String chartName, String bottomAxisName, String leftAxisName, EmbeddedObjectPosition chartPosition, ArrayList<BasicChartDomain> chartDomains, ArrayList<BasicChartSeries> chartSeries) {
        this.chart = chart;
        this.startRowIndexWithOffset = startRowIndexWithOffset;
        this.chartName = chartName;
        this.bottomAxisName = bottomAxisName;
        this.leftAxisName = leftAxisName;
        this.chartPosition = chartPosition;
        this.chartDomains = chartDomains;
        this.chartSeries = chartSeries;
    }

    public static class ChartBuilder
    {
        @Value("${sheet.name}")
        private String sheetName;
        @Value("${sheet.isFirst}")
        private Boolean isFirstSheetFlag;
        @Value("${sheet.gid}")
        private Integer sheetGid;
        private String rangeSuffix;
        private Sheets sheetsService;
        private final String SPREADSHEET_ID = "1iNzLpyVqGmnHeyOU-VkjsqhUca2g7RPdtMFJNLUPQaU"; // TO DO - moving this variable maybe to the yaml with config

        @PostConstruct
        public void init() throws GeneralSecurityException, IOException {
            this.rangeSuffix = isFirstSheetFlag ? "" : String.format("%s!", sheetName);
            this.sheetsService = SheetsServicePreparer.getSheetsService();
            this.chartDomains = new ArrayList<>();
            this.chartSeries = new ArrayList<>();
        }

        private List<Request> chart;
        private Integer startRowIndexWithOffset;
        private String chartName;
        private String bottomAxisName;
        private String leftAxisName;
        private EmbeddedObjectPosition chartPosition;
        private ArrayList<BasicChartDomain> chartDomains;
        private ArrayList<BasicChartSeries> chartSeries;

        public ChartBuilder setStartRowIndexWithOffset(Integer startIndexOffset) throws IOException {
            this.startRowIndexWithOffset = sheetsService // Chart start row = all rows + 1
                    .spreadsheets()
                    .values()
                    .get(SPREADSHEET_ID, sheetName)
                    .execute()
                    .getValues()
                    .size() + startIndexOffset; // 1

            return this;
        }

        public ChartBuilder setChartName(String chartName) {
            this.chartName = chartName;

            return this;
        }

        public ChartBuilder setBottomAxisName(String bottomAxisName) {
            this.bottomAxisName = bottomAxisName;

            return this;
        }

        public ChartBuilder setLeftAxisName(String leftAxisName) {
            this.leftAxisName = leftAxisName;

            return this;
        }

        public ChartBuilder setChartPosition(Integer chartStartRow, Integer columnIndex) {
            this.chartPosition = new EmbeddedObjectPosition()
                    .setOverlayPosition(new OverlayPosition()
                            .setAnchorCell(new GridCoordinate()
                                    .setSheetId(sheetGid)
                                    .setRowIndex(chartStartRow)
                                    .setColumnIndex(columnIndex)));

            return this;
        }

        public ChartBuilder addChartDomain(Integer startRowIndex, Integer endRowIndex, Integer startColumnIndex, Integer endColumnIndex) {
            this.chartDomains.add(new BasicChartDomain()
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

            return this;
        }

        public ChartBuilder addChartSeries(Integer startRowIndex, Integer endRowIndex, Integer startColumnIndex, Integer endColumnIndex) {
            this.chartSeries.add(new BasicChartSeries()
                    .setSeries(new ChartData()
                            .setSourceRange(new ChartSourceRange()
                                    .setSources(Arrays.asList(
                                            new GridRange()
                                                    .setSheetId(sheetGid)
                                                    .setStartRowIndex(startRowIndex) // 1
                                                    .setEndRowIndex(endRowIndex) // 7
                                                    .setStartColumnIndex(startColumnIndex) // 1,2,3
                                                    .setEndColumnIndex(endColumnIndex) // 2,3,4
                                    ))))
                    .setTargetAxis("LEFT_AXIS"));

            return this;
        }

        public Chart build() {
            this.chart = new ArrayList<Request>() {{
                add(new Request().setAddChart(new AddChartRequest().setChart(new EmbeddedChart()
                        .setSpec(new ChartSpec()
                                .setTitle(chartName) // "Computer parts prices"
                                .setBasicChart(new BasicChartSpec()
                                        .setChartType("COLUMN")
                                        .setLegendPosition("BOTTOM_LEGEND")
                                        .setAxis(new ArrayList<>() {{
                                            add(new BasicChartAxis()
                                                    .setPosition("BOTTOM_AXIS")
                                                    .setTitle(bottomAxisName)); // Date
                                            add(new BasicChartAxis()
                                                    .setPosition("LEFT_AXIS")
                                                    .setTitle(leftAxisName));  // Price
                                        }})
                                        .setDomains(chartDomains)
                                        .setSeries(chartSeries)))
                        .setPosition(chartPosition))));
            }};

            return new Chart(chart, startRowIndexWithOffset, chartName, bottomAxisName, leftAxisName, chartPosition, chartDomains, chartSeries);
        }
    }
}
