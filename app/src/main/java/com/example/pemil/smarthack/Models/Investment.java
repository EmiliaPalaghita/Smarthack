package com.example.pemil.smarthack.Models;

import java.util.ArrayList;
import java.util.List;

public class Investment {

    private String name;
    private String date;
    private String amt;
    private Double close;
    private Double high;
    private String industry;
    private Double low;
    private Double open;
    private Double predictedValue;
    private String sector;
    private Double volum;

    public Investment(String name, String date,
                      String amt, Double close, Double high,
                      String industry, Double low, Double open,
                      Double predictedValue, String sector, Double volum) {
        this.name = name;
        this.date = date;
        this.amt = amt;
        this.close = close;
        this.high = high;
        this.industry = industry;
        this.low = low;
        this.open = open;
        this.predictedValue = predictedValue;
        this.sector = sector;
        this.volum = volum;
    }

    public Investment() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(Double predictedValue) {
        this.predictedValue = predictedValue;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Double getVolum() {
        return volum;
    }

    public void setVolum(Double volum) {
        this.volum = volum;
    }

    @Override
    public String toString() {
        return "Investment{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", amt='" + amt + '\'' +
                ", close=" + close +
                ", high=" + high +
                ", industry='" + industry + '\'' +
                ", low=" + low +
                ", open=" + open +
                ", predictedValue=" + predictedValue +
                ", sector='" + sector + '\'' +
                ", volum=" + volum +
                '}';
    }
}
