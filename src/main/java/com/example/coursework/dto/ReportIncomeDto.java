package com.example.coursework.dto;

public class ReportIncomeDto {

    private String advertising;
    private long count;
    private double totalIncome;

    public ReportIncomeDto()
    {

    }

    public ReportIncomeDto(String advertising) {
        this.advertising = advertising;
        count = 0;
        totalIncome = 0.0;
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
