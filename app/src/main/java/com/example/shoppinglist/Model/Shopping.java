package com.example.shoppinglist.Model;

public class Shopping {
    String name;
    int qtd;
    double price;
    String id;

    public Shopping(){

    }

    public Shopping(String name, int qtd, double price, String id) {
        this.name = name;
        this.qtd = qtd;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
