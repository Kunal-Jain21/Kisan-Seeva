package com.example.kisanseeva.Mandi;

public class Crop {
    private String state;
    private String district;
    private String market;
    private String commodity;
    private String variety;
    private String arrival_date;
    private String min_price;
    private String max_price;
    private String modal_price;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Crop(String state, String market, String commodity, String arrival_date, String modal_price) {
        this.state = state;
        this.market = market;
        this.commodity = commodity;
        this.arrival_date = arrival_date;
        this.modal_price = modal_price;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getModal_price() {
        return modal_price;
    }

    public void setModal_price(String modal_price) {
        this.modal_price = modal_price;
    }
}