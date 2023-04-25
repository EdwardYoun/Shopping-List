package edu.uga.cs.shoppinglist;

public class Item {
    private String itemName;
    private String priceCost;
    private String id;

    public Item(){}

    public Item(String id, String itemName, String priceCost) {
        this.itemName = itemName;
        this.priceCost = priceCost;
        this.id = id;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPriceCost() { return priceCost; }

    public void setPriceCost(String priceCost) {
        this.priceCost = priceCost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

