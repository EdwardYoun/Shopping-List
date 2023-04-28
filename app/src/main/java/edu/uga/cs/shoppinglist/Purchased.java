package edu.uga.cs.shoppinglist;

import java.util.ArrayList;

public class Purchased {

    private ArrayList<Item> itemList;
    private double total;
    private String user;
    private String id;

    public Purchased(){}

    public Purchased(String id, ArrayList<Item> itemList, double total, String user) {
        this.itemList = itemList;
        this.total = total;
        this.user = user;
        this.id = id;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public double getTotal() { return total; }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
