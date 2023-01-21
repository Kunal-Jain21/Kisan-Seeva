package com.example.kisanseeva.Renting.GiveOnRent;

public class productModel {
    private String prod_id, prod_name, prod_desc;
    private int prod_price, prod_img;

    public productModel(String prod_id, String prod_name, String prod_desc, int prod_price, int prod_img) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_desc = prod_desc;
        this.prod_price = prod_price;
        this.prod_img = prod_img;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public int getProd_price() {
        return prod_price;
    }

    public void setProd_price(int prod_price) {
        this.prod_price = prod_price;
    }

    public int getProd_img() {
        return prod_img;
    }

    public void setProd_img(int prod_img) {
        this.prod_img = prod_img;
    }
}
