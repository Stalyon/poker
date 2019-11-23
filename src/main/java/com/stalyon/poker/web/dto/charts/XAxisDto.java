package com.stalyon.poker.web.dto.charts;

import java.util.List;

public class XAxisDto {

    private String type;
    private List<String> data;

    public XAxisDto(String type, List<String> data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
