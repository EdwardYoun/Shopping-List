package edu.uga.cs.shoppinglist;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PurchasedAdapter extends ArrayAdapter<Purchased> implements ListAdapter {

    private ArrayList<Purchased> purchasedList;
    private DatabaseReference purchasedReference;
    public PurchasedAdapter(Context context, ArrayList<Purchased> items) {
        super(context, 0, items);
        purchasedList = items;
    }

    @Override
    public Purchased getItem(int position) {
        return purchasedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Purchased purchased = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_purchased, parent, false);
        }

        String group = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView recentView = convertView.findViewById(R.id.textView6);
        TextView itemsView = convertView.findViewById(R.id.textView7);
        TextView userView = convertView.findViewById(R.id.textView9);
        EditText totalView = convertView.findViewById(R.id.textView8);
        Button priceButton = convertView.findViewById(R.id.button14);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        recentView.setVisibility(View.GONE);

        for (int i = 0; i < purchasedList.get(position).getItemList().size(); i++) {
            if (i == 0) {
                group = purchasedList.get(position).getItemList().get(i).getItemName();
            }
            else {
                group = group + ", " + purchasedList.get(position).getItemList().get(i).getItemName();
            }
        }
        itemsView.setText(group);
        totalView.setText(Integer.toString(purchasedList.get(position).getTotal()));
        userView.setText(purchasedList.get(position).getUser());

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newTotal = totalView.getText().toString();

                if (!newTotal.isEmpty()) {
                    purchasedReference.child(purchasedList.get(position).getId()).child("total").setValue(Integer.parseInt(newTotal));
                }
                else {
                    totalView.setText(Integer.toString(purchasedList.get(position).getTotal()));
                    Toast.makeText(v.getContext(),
                            "Cannot update with blank field!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

}
