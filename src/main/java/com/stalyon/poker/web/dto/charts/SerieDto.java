package com.stalyon.poker.web.dto.charts;

import java.util.List;

public class SerieDto {

    private String type;
    private String symbol;
    private List<Double> data;

    public SerieDto(String type) {
        this.type = type;
    }

    public SerieDto(String type, List<Double> data) {
        this.type = type;
        this.data = data;
    }

    public SerieDto(String type, String symbol, List<Double> data) {
        this.type = type;
        this.symbol = symbol;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }
}
