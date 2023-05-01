package edu.uga.cs.shoppinglist;

public class Item {
    private String itemName;
    private String priceCost;
    private String id;

    public Item(){}

    /**
     * Constructs new Item object with specified id, name and cost.
     * @param id Id for list position
     * @param itemName Item's name
     * @param priceCost Price of the item
     */
    public Item(String id, String itemName, String priceCost) {
        this.itemName = itemName;
        this.priceCost = priceCost;
        this.id = id;
    }

    /**
     * @return the name of the item
     */
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the cost of the item
     */
    public String getPriceCost() { return priceCost; }

    public void setPriceCost(String priceCost) {
        this.priceCost = priceCost;
    }

    /**
     * @return the item's id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id for setting the id of the item
     */
    public void setId(String id) {
        this.id = id;
    }
}

