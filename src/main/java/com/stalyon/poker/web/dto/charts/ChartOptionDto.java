package com.stalyon.poker.web.dto.charts;

import java.util.List;

public class ChartOptionDto {

    private XAxisDto xAxis;
    private YAxisDto yAxis;
    private List<SerieDto> series;

    public ChartOptionDto(XAxisDto xAxis, YAxisDto yAxis, List<SerieDto> series) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.series = series;
    }

    public XAxisDto getxAxis() {
        return xAxis;
    }

    public void setxAxis(XAxisDto xAxis) {
        this.xAxis = xAxis;
    }

    public YAxisDto getyAxis() {
        return yAxis;
    }

    public void setyAxis(YAxisDto yAxis) {
        this.yAxis = yAxis;
    }

    public List<SerieDto> getSeries() {
        return series;
    }

    public void setSeries(List<SerieDto> series) {
        this.series = series;
    }
}
