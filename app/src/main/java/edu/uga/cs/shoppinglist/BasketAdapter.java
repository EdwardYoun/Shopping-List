package edu.uga.cs.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BasketAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference databaseReference, basketReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userEmail = user.getEmail().replace(".","/");

    /**
     * Adapter for the Shopping Basket.
     * @param context Context of app.
     * @param itemList List of items in shopping basket.
     */
    public BasketAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    /**
     * @return size of the list of items in the Shopping list.
     */
    @Override
    public int getCount() {
        return itemList.size();
    }

    /**
     * @param position Position in the list.
     * @return item at postion.
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method gets the view of the item in the Shopping Basket.
     * If the view is null, then it inflates the layout of the item in the Shopping Basket.
     * It sets the text of the item name and its price.
     * It also sets the visibility of the add, return, basket, checkout, edit, delete and buy button to gone.
     * If remove button is clicked, it removes the item from the Shopping Basket and adds it back to the Shopping List.
     * @param position Position in shopping list.
     * @param convertView The view to be recycled.
     * @param parent The parent view the item will be attached to.
     * @return List of items added to the shopping basket
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
        ImageButton editButton = convertView.findViewById(R.id.button5);
        ImageButton deleteButton = convertView.findViewById(R.id.button10);
        ImageButton buyButton = convertView.findViewById(R.id.button11);
        Button removeButton = convertView.findViewById(R.id.button13);
        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        basketReference = FirebaseDatabase.getInstance().getReference("basket" + userEmail);

        //prevent items from being editable
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

        //removes item from basket and puts it back into the list
        removeButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method gets the view of the item in the Shopping Basket.
             * If the view is null, then it inflates the layout of the item in the Shopping Basket.
             * It sets the text of the item name and its price.
             * It also sets the visibility of the add, return, basket, checkout, edit, delete and buy button to gone.
             * If remove button is clicked, it removes the item from the Shopping Basket and adds it back to the Shopping List.
             * @param v View of the remove button.
             */
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

