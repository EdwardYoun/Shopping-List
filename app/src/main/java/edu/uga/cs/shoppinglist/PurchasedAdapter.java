package edu.uga.cs.shoppinglist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PurchasedAdapter extends ArrayAdapter<Purchased> implements ListAdapter {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private ArrayList<Purchased> purchasedList;
    private DatabaseReference purchasedReference;

    /**
     * This is the constructor of the PurchasedAdapter class.
     * It initializes the adapter with the given list of Purchased objects.
     * @param context Context of adapter
     * @param items list of Purchased objects to be displayed.
     */
    public PurchasedAdapter(Context context, ArrayList<Purchased> items) {
        super(context, 0, items);
        purchasedList = items;
    }

    /**
     * Retrieves the Purchased object at the given position in the adapter's list.
     * @param position position of the desired Purchased object in the list.
     * @return Purchased object at the given position in the list.
     */
    @Override
    public Purchased getItem(int position) {
        return purchasedList.get(position);
    }

    /**
     * Retrieves the ID of the item at the given position in the adapter's list.
     * @param position Position of Id in the list
     * @return Id of item at the position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the View for a specific item in the ListView.
     * @param position position of the item within the adapter's data set.
     * @param convertView old view to reuse, if possible.
     * @param parent parent that this view will eventually be attached to.
     * @return View corresponding to the specified position.
     */
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
        ImageButton priceButton = convertView.findViewById(R.id.button14);
        ImageButton editButton = convertView.findViewById(R.id.button15);
        ImageButton backButton = convertView.findViewById(R.id.goBackButton2);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        recentView.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        for (int i = 0; i < purchasedList.get(position).getItemList().size(); i++) {
            if (i == 0) {
                group = purchasedList.get(position).getItemList().get(i).getItemName();
            }
            else {
                group = group + ", " + purchasedList.get(position).getItemList().get(i).getItemName();
            }
        }
        itemsView.setText(group);
        totalView.setText(String.format("%.2f", purchasedList.get(position).getTotal()));
        userView.setText(purchasedList.get(position).getUser());

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTotal = totalView.getText().toString();

                try {
                    if (!newTotal.isEmpty()) {
                        Double doub = Double.valueOf(newTotal);
                        doub = Math.round(doub * 100.0) / 100.0;
                        purchasedReference.child(purchasedList.get(position).getId()).child("total").setValue(doub);
                        totalView.setText(String.format("%.2f", doub));
                    } else {
                        totalView.setText(Double.toString(purchasedList.get(position).getTotal()));
                        Toast.makeText(v.getContext(),
                                "Cannot update with blank field!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    totalView.setText(Double.toString(purchasedList.get(position).getTotal()));
                    Toast.makeText(v.getContext(),
                            "Price must be a number!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener for the edit button on the purchased list.
             * Opens the GroupActivity for the list of items in the group on the purchased list.
             * @param v
             */
            @Override
            public void onClick(View v) {
                Bundle extra = new Bundle();
                extra.putString("position", purchasedList.get(position).getId());
                Intent intent = new Intent(getContext(), GroupActivity.class);
                intent.putExtras(extra);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

}
