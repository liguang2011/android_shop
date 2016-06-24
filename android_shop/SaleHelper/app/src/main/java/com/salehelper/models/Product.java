package com.salehelper.models;


public class Product {
    private int productid;
    private String name;
    private double price;
    private String describe;
    private int num;
    private String image;

    public Product(int productid, String name, double price, String describe, int num, String image) {
        this.productid = productid;
        this.name = name;
        this.price = price;
        this.describe = describe;
        this.num = num;
        this.image = image;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
