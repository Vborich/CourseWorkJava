package com.example.coursework.dto;

import java.util.ArrayList;

public class ReportDemandDto {

    private String advertising;
    private ArrayList<Integer> monthsCounts;

    public ReportDemandDto(String advertising) {
        this.advertising = advertising;
        monthsCounts = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            monthsCounts.add(0);
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public ArrayList<Integer> getMonthsCounts() {
        return monthsCounts;
    }

    public void setMonthsCounts(ArrayList<Integer> monthsCounts) {
        this.monthsCounts = monthsCounts;
    }
}
