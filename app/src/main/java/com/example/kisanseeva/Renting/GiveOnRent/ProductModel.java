package com.example.kisanseeva.Renting.GiveOnRent;

public class ProductModel {
    private String prod_id, prod_name, prod_desc, prod_img, category, giver_id;
    private int prod_price;

    public String getGiver_id() {
        return giver_id;
    }

    public void setGiver_id(String giver_id) {
        this.giver_id = giver_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ProductModel() {
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

    public String getProd_img() {
        return prod_img;
    }

    public void setProd_img(String prod_img) {
        this.prod_img = prod_img;
    }
}
