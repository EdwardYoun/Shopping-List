package edu.uga.cs.shoppinglist;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PurchasedAdapter extends ArrayAdapter<Item> implements ListAdapter {

    private ArrayList<Item> purchasedList;
    public PurchasedAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        purchasedList = items;
    }

    @Override
    public Item getItem(int position) {
        return purchasedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_purchased, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView costTextView = convertView.findViewById(R.id.priceCost);


        if (item.getItemName() != null && item.getPriceCost() != null) {
            nameTextView.setText(item.getItemName());
            costTextView.setText(String.valueOf(item.getPriceCost()));
        }

        return convertView;
    }

}
