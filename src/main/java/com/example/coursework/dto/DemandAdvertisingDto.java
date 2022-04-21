package com.example.coursework.dto;

public class DemandAdvertisingDto {

    private String advertising;
    private long value;

    public DemandAdvertisingDto(String advertising, long valie) {
        this.advertising = advertising;
        this.value = valie;
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
