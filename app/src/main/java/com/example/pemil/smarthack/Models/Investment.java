package com.example.pemil.smarthack.Models;

import java.util.ArrayList;
import java.util.List;

public class Investment {

    private String name;
    private String date;
    private String amt;
    private String category;
    private String subCategory;
    private String profit;

    public Investment(String name, String date, String amt, String category, String subCategory, String profit) {
        this.name = name;
        this.date = date;
        this.amt = amt;
        this.category = category;
        this.subCategory = subCategory;
        this.profit = profit;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "Investment{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", amt='" + amt + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", profit='" + profit + '\'' +
                '}';
    }
}
