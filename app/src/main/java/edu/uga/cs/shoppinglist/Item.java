package edu.uga.cs.shoppinglist;

public class Item {
    private String itemName;
    private String priceCost;


    public Item(String itemName, String priceCost) {
        this.itemName = itemName;
        this.priceCost = priceCost;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPriceCost() {
        return priceCost;
    }

    public void setPriceCost(String priceCost) {
        this.priceCost = priceCost;
    }
}

