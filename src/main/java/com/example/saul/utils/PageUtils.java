package com.example.saul.utils;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


public class PageUtils {
    private int total;
    @JsonManagedReference
    private List<?> rows;

    public PageUtils() {

    }

    public PageUtils(int total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

}

