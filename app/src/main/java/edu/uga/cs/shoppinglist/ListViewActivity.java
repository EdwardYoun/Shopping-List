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

                if (!itemName.isEmpty() && !priceCost.isEmpty()) {
                    String itemId = databaseReference.push().getKey();
                    Item item = new Item(itemId, itemName, priceCost);
                    databaseReference.child(itemId).setValue(item);
                    itemNameEditText.setText("");
                    priceCostEditText.setText("");
                    //itemAdapter.notifyDataSetChanged();
                }
            }
        });

        // Listen for changes in the database and update the list adapter with new data
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
            public void onClick(View view){
                ArrayList<Item> purchasedList = new ArrayList<>();

                if(!basketList.isEmpty()) {
                    for (int i = 0; i < basketList.size(); i++) {
                        Item item = new Item(basketList.get(i).getId(), basketList.get(i).getItemName(), basketList.get(i).getPriceCost());
                        purchasedReference.child(basketList.get(i).getId()).setValue(item);
                    }
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
