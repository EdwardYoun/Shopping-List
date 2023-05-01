package edu.uga.cs.shoppinglist;

import android.content.ClipData;
import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import edu.uga.cs.shoppinglist.Item;

public class ItemAdapter extends BaseAdapter {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference databaseReference, basketReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userEmail = user.getEmail().replace(".","/");

    /**
     * Constructs new ItemAdapter with context and itemList
     * @param context context for adapter
     * @param itemList list to be used
     */
    public ItemAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    /**
     * @return number of items in the list
     */
    @Override
    public int getCount() { return itemList.size(); }

    /**
     * @param position position in the list
     * @return item object at the specified position
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    /**
     * @param position position in the list
     * @return Id of the item at the specified position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets up each item in the ShoppingList
     * Adds an edit, delete, and buy buttons for their respective functions on the ShoppingList.
     * @param position position of the item
     * @param convertView View of the item
     * @param parent parent Shopping List
     * @return the View for the item
     */
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
        basketReference = FirebaseDatabase.getInstance().getReference("basket" + userEmail);

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
                try {
                    if (!newItemName.isEmpty() && !newPriceCost.isEmpty()) {
                        Double doub = Double.valueOf(newPriceCost);
                        databaseReference.child(itemList.get(position).getId()).child("itemName").setValue(newItemName);
                        databaseReference.child(itemList.get(position).getId()).child("priceCost").setValue(String.format("%.2f", doub));
                        itemNameTextView.setText(newItemName);
                        priceCostTextView.setText(String.format("%.2f", doub));
                    } else {
                        itemNameTextView.setText(itemList.get(position).getItemName());
                        priceCostTextView.setText(itemList.get(position).getPriceCost());
                        Toast.makeText(v.getContext(),
                                "Cannot update with blank fields!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    itemNameTextView.setText(itemList.get(position).getItemName());
                    priceCostTextView.setText(itemList.get(position).getPriceCost());
                    Toast.makeText(v.getContext(),
                            "Price must be a number!",
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

