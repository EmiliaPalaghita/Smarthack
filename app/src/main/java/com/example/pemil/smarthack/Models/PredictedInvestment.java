package com.example.pemil.smarthack.Models;

public class PredictedInvestment {
    private Double close;
    private Double high;
    private String industry;
    private Double low;
    private Double open;
    private Double predictedValue;
    private String sector;
    private Double volum;

    public PredictedInvestment() {
    }

    public PredictedInvestment(Double close, Double high, String industry, Double low, Double open, Double predictedValue, String sector, Double volum) {
        this.close = close;
        this.high = high;
        this.industry = industry;
        this.low = low;
        this.open = open;
        this.predictedValue = predictedValue;
        this.sector = sector;
        this.volum = volum;
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
        return "PredictedInvestment{" +
                "close=" + close +
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
