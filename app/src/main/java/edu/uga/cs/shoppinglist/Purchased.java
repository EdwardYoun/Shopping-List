package edu.uga.cs.shoppinglist;

import java.util.ArrayList;

public class Purchased {

    private ArrayList<Item> itemList;
    private int total;
    private String id;

    public Purchased(){}

    public Purchased(String id, ArrayList<Item> itemList, int total) {
        this.itemList = itemList;
        this.total = total;
        this.id = id;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public int getTotal() { return total; }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
