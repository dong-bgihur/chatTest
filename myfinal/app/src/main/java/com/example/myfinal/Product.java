package com.example.myfinal;

public class Product {
    public  Product (String id,String name,String type,int price,int quantity,String img){

        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.img = img;
    }
    public Product(){}

    public String id;
    public String name;
    public String type;
    public int price;
    public int quantity;
    public String img;
}