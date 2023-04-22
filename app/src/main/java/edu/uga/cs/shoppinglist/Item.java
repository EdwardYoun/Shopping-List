package edu.uga.cs.shoppinglist;

public class Item {
    private String itemName;
    private String priceCost;
    private boolean onList;

    public Item(){}

    public Item(String itemName, String priceCost) {
        this.itemName = itemName;
        this.priceCost = priceCost;
        this.onList = false;
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

    // added this method
    public boolean isAddedToList() {
        return onList;
    }

    // added this method
    public void setAddedToList(boolean addedToList) {
        this.onList = onList;
    }
}

