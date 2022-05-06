package com.example.biztositas;

public class Insurance {

    private String company;
    private String period;
    private int price;


    public Insurance() {
    }

    public Insurance(String company, String period, int price) {
        this.company = company;
        this.period = period;
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
