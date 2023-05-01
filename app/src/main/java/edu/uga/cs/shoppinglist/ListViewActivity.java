package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {


    private EditText itemNameEditText, priceCostEditText;
    private Button addButton, removeButton;
    private ImageButton editButton, deleteButton, buyButton;
    private ListView listView, basketView;
    private ArrayList<Item> itemList, basketList;
    private ItemAdapter itemAdapter;
    private BasketAdapter basketAdapter;
    private DatabaseReference databaseReference, basketReference, purchasedReference;

    private FirebaseDatabase database;

    /**
     * Sets up the ListViewActivity which displays the shopping list.
     * Allows the user to add items and their price to the list.
     * They can then add the items to their basket and checkout.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        itemNameEditText = findViewById(R.id.item_name);
        priceCostEditText = findViewById(R.id.priceCost);
        addButton = findViewById(R.id.add_button );
        listView = findViewById(R.id.list_view);
        basketView = findViewById(R.id.basket_view);

        editButton = findViewById(R.id.button5);
        deleteButton = findViewById(R.id.button10);
        buyButton = findViewById(R.id.button11);
        removeButton = findViewById(R.id.button13);

        editButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        buyButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);
        listView.setAdapter(itemAdapter);

        basketList = new ArrayList<>();
        basketAdapter = new BasketAdapter(this, basketList);
        basketView.setAdapter(basketAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        basketReference = FirebaseDatabase.getInstance().getReference("basket");
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String itemName = itemNameEditText.getText().toString();
                final String priceCost = priceCostEditText.getText().toString();
                try {
                    if (!itemName.isEmpty() && !priceCost.isEmpty()) {
                        Double doub = Double.valueOf(priceCost);
                        String itemId = databaseReference.push().getKey();
                        Item item = new Item(itemId, itemName, String.format("%.2f", doub));
                        databaseReference.child(itemId).setValue(item);
                        itemNameEditText.setText("");
                        priceCostEditText.setText("");
                        //itemAdapter.notifyDataSetChanged();
                    }
                    else {
                        itemNameEditText.setText("");
                        priceCostEditText.setText("");
                        Toast.makeText(v.getContext(),
                                "Cannot add with blank fields!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    itemNameEditText.setText("");
                    priceCostEditText.setText("");
                    Toast.makeText(v.getContext(),
                            "Price must be a number!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        basketReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                basketList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    basketList.add(item);
                }
                basketAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton1);
        backToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!basketList.isEmpty()) {
                    for (int i = 0; i < basketList.size(); i++) {
                        Item item = new Item(basketList.get(i).getId(), basketList.get(i).getItemName(), basketList.get(i).getPriceCost());
                        databaseReference.child(basketList.get(i).getId()).setValue(item);
                    }
                    basketReference.removeValue();
                }
                Intent intent = new Intent(ListViewActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
        Button checkoutButton = findViewById(R.id.button12);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!basketList.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    double total = 0;
                    String listId = purchasedReference.push().getKey();
                    for (int i = 0; i < basketList.size(); i++) {
                        total = total + Double.parseDouble(basketList.get(i).getPriceCost());
                    }
                    total = total * 1.049;
                    total = Math.round(total * 100.0) / 100.0;
                    Purchased purchased = new Purchased(listId, basketList, total, user.getEmail());
                    purchasedReference.child(listId).setValue(purchased);
                    basketReference.removeValue();
                    Intent intent = new Intent(ListViewActivity.this, PurchasedActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(view.getContext(),
                            "Cannot checkout with zero items!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * When the user presses the back button to return to the homepage,
     * The items currently in their basket are added back to the shopping list.
     */
    @Override
    public void onBackPressed() {
        if (!basketList.isEmpty()) {
            for (int i = 0; i < basketList.size(); i++) {
                Item item = new Item(basketList.get(i).getId(), basketList.get(i).getItemName(), basketList.get(i).getPriceCost());
                databaseReference.child(basketList.get(i).getId()).setValue(item);
            }
            basketReference.removeValue();
        }
        Intent intent = new Intent(ListViewActivity.this, UserActivity.class);
        startActivity(intent);
    }

}
