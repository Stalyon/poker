package com.stalyon.poker.web.dto.charts;

import java.util.List;

public class YAxisDto {

    private String type;
    private List<String> boundaryGap;

    public YAxisDto(String type) {
        this.type = type;
    }

    public YAxisDto(String type, List<String> boundaryGap) {
        this.type = type;
        this.boundaryGap = boundaryGap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getBoundaryGap() {
        return boundaryGap;
    }

    public void setBoundaryGap(List<String> boundaryGap) {
        this.boundaryGap = boundaryGap;
    }
}
