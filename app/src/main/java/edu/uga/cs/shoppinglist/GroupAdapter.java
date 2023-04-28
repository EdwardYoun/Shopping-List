package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference purchasedReference, databaseReference;
    private String id;
    private Double total;

    public GroupAdapter(Context context, ArrayList<Item> itemList, String id) {
        this.context = context;
        this.itemList = itemList;
        this.id = id;
        FirebaseDatabase.getInstance().getReference("purchased").child(id).child("total").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = snapshot.getValue(Double.class);
            }


            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read value.", error.toException());
            }
        });

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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_group, parent, false);
        }

        TextView itemView = convertView.findViewById(R.id.textView10);
        TextView itemCost = convertView.findViewById(R.id.textView25);
        Button removeButton = convertView.findViewById(R.id.button16);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        ImageButton backButton = convertView.findViewById(R.id.goBackButton3);

        backButton.setVisibility(View.GONE);

        itemView.setText(itemList.get(position).getItemName());
        itemCost.setText("$ " + itemList.get(position).getPriceCost());
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedReference.child(id).child("itemList").child(Integer.toString(position)).removeValue();
                databaseReference.child(itemList.get(position).getId()).setValue(itemList.get(position));
                if (itemList.size() == 1) {
                    purchasedReference.child(id).removeValue();
                    Intent intent = new Intent(v.getContext(), PurchasedActivity.class);
                    v.getContext().startActivity(intent);
                }
                else if (position < itemList.size()-1) {
                    for (int i = position; i < itemList.size()-1; i++) {
                        purchasedReference.child(id).child("itemList").child(Integer.toString(i)).child("id").setValue(itemList.get(i+1).getId());
                        purchasedReference.child(id).child("itemList").child(Integer.toString(i)).child("itemName").setValue(itemList.get(i+1).getItemName());
                        purchasedReference.child(id).child("itemList").child(Integer.toString(i)).child("priceCost").setValue(itemList.get(i+1).getPriceCost());
                        itemView.setText(itemList.get(i+1).getItemName());
                        purchasedReference.child(id).child("itemList").child(Integer.toString(i+1)).removeValue();
                        Double cost = Double.parseDouble(itemList.get(position).getPriceCost());
                        total -= cost;
                        purchasedReference.child(id).child("total").setValue(total);

                    }
                }
            }
        });

        return convertView;
    }

}