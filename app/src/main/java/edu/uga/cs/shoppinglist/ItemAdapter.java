package edu.uga.cs.shoppinglist;

import android.content.ClipData;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import edu.uga.cs.shoppinglist.Item;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference databaseReference, basketReference;

    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() { return itemList.size(); }

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
        Button removeButton = convertView.findViewById(R.id.button13);
        ImageButton editButton = convertView.findViewById(R.id.button5);
        ImageButton deleteButton = convertView.findViewById(R.id.button10);
        ImageButton buyButton = convertView.findViewById(R.id.button11);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        basketReference = FirebaseDatabase.getInstance().getReference("basket");

        itemNameTextView.setText(itemList.get(position).getItemName());
        priceCostTextView.setText(itemList.get(position).getPriceCost());

        addButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.GONE);
        basket.setVisibility(View.GONE);
        checkoutButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newItemName = itemNameTextView.getText().toString();
                final String newPriceCost = priceCostTextView.getText().toString();

                if (!newItemName.isEmpty() && !newPriceCost.isEmpty()) {
                    databaseReference.child(itemList.get(position).getId()).child("itemName").setValue(newItemName);
                    databaseReference.child(itemList.get(position).getId()).child("priceCost").setValue(newPriceCost);
                }
                else {
                    itemNameTextView.setText(itemList.get(position).getItemName());
                    priceCostTextView.setText(itemList.get(position).getPriceCost());
                    Toast.makeText(v.getContext(),
                            "Cannot update with blank fields!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(itemList.get(position).getId()).removeValue();
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(itemList.get(position).getId(), itemList.get(position).getItemName(), itemList.get(position).getPriceCost());
                basketReference.child(itemList.get(position).getId()).setValue(item);
                databaseReference.child(itemList.get(position).getId()).removeValue();
            }
        });

        return convertView;
    }

}

