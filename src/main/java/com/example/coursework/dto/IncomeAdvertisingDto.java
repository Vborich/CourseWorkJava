package com.example.coursework.dto;

import java.util.ArrayList;

public class IncomeAdvertisingDto {

    private String advertising;
    private ArrayList<Double> monthsValues;

    public IncomeAdvertisingDto()
    {

    }

    public IncomeAdvertisingDto(String advertising) {
        this.advertising = advertising;
        monthsValues = new ArrayList<>();
        for(int i = 0; i < 12; i++)
            monthsValues.add(0.0);
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public ArrayList<Double> getMonthsValues() {
        return monthsValues;
    }

    public void setMonthsValues(ArrayList<Double> monthsValues) {
        this.monthsValues = monthsValues;
    }
}
