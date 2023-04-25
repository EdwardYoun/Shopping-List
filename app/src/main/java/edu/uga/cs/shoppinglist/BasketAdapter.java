package edu.uga.cs.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BasketAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference databaseReference, basketReference;

    public BasketAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_view, parent, false);
        }

        TextView itemNameTextView = convertView.findViewById(R.id.item_name);
        TextView priceCostTextView = convertView.findViewById(R.id.priceCost);
        Button addButton = convertView.findViewById(R.id.add_button);
        ImageButton returnButton = convertView.findViewById(R.id.goBackButton1);
        TextView basket = convertView.findViewById(R.id.textView5);
        Button checkoutButton = convertView.findViewById(R.id.button12);
        ImageButton editButton = convertView.findViewById(R.id.button5);
        ImageButton deleteButton = convertView.findViewById(R.id.button10);
        ImageButton buyButton = convertView.findViewById(R.id.button11);
        Button removeButton = convertView.findViewById(R.id.button13);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        basketReference = FirebaseDatabase.getInstance().getReference("basket");

        itemNameTextView.setText(itemList.get(position).getItemName());
        itemNameTextView.setFocusable(false);
        priceCostTextView.setText(itemList.get(position).getPriceCost());
        priceCostTextView.setFocusable(false);

        addButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.GONE);
        basket.setVisibility(View.GONE);
        checkoutButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        buyButton.setVisibility(View.GONE);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(itemList.get(position).getId(), itemList.get(position).getItemName(), itemList.get(position).getPriceCost());
                databaseReference.child(itemList.get(position).getId()).setValue(item);
                basketReference.child(itemList.get(position).getId()).removeValue();
            }
        });

        return convertView;
    }

}

