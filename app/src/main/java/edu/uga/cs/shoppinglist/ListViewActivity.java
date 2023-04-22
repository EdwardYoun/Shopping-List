package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {


    private EditText itemNameEditText, priceCostEditText;
    private Button addButton;
    private ListView listView;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;
    private DatabaseReference databaseReference;

    private FirebaseDatabase database;
    private Item currentItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        itemNameEditText = findViewById(R.id.item_name);
        priceCostEditText = findViewById(R.id.priceCost);
        addButton = findViewById(R.id.add_button );
        listView = findViewById(R.id.list_view);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList);
        listView.setAdapter(itemAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String itemName = itemNameEditText.getText().toString();
                final String priceCost = priceCostEditText.getText().toString();

                if (!itemName.isEmpty() && !priceCost.isEmpty()) {
                    String itemId = databaseReference.push().getKey();
                    Item item = new Item(itemName, priceCost);
                    databaseReference.child(itemId).setValue(item);
                    itemList.add(item);
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
        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton1);
        backToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListViewActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }

}
