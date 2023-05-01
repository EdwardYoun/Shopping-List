package edu.uga.cs.shoppinglist;

import java.util.ArrayList;

public class Purchased {

    private ArrayList<Item> itemList;
    private double total;
    private String user;
    private String id;

    public Purchased(){}

    /**
     * Purchased constructor for a purchased object
     * @param id the id of the purchased itemList
     * @param itemList the list of purchased items
     * @param total the number of purchased items in this object
     * @param user the user who purchased these items
     */
    public Purchased(String id, ArrayList<Item> itemList, double total, String user) {
        this.itemList = itemList;
        this.total = total;
        this.user = user;
        this.id = id;
    }

    /**
     * @return the list of items that were purchased
     */
    public ArrayList<Item> getItemList() {
        return itemList;
    }

    /**
     * @param itemList
     */
    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * @return the number of items in that were purchased
     */
    public double getTotal() { return total; }

    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the id of the list of purchased items
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id of the list of purchased items
     * @param id id of the purchased items list
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the user's email that purchased the items
     */
    public String getUser() {
        return user;
    }

    /**
     * set the email of purchased items
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }
}
