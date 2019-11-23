package com.stalyon.poker.web.dto;

import com.stalyon.poker.web.dto.charts.ChartOptionDto;

public class StatsDto {

    private ChartOptionDto chartOption;

    public StatsDto(ChartOptionDto chartOption) {
        this.chartOption = chartOption;
    }

    public ChartOptionDto getChartOption() {
        return chartOption;
    }

    public void setChartOption(ChartOptionDto chartOption) {
        this.chartOption = chartOption;
    }
}
