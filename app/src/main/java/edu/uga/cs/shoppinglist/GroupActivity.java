package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private ArrayList<Item> itemList;
    private GroupAdapter groupAdapter;
    private ListView groupView;
    private TextView textView, itemCost;
    private Button removeButton;
    private DatabaseReference purchasedReference;

    /**
     * Sets up the GroupActivity and initializes the item list and adapter. This is a list of purchased items.
     * Also listens for changes in the Firebase database.
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");
        groupView = findViewById(R.id.groupList);
        textView = findViewById(R.id.textView10);
        itemCost = findViewById(R.id.textView25);
        removeButton = findViewById(R.id.button16);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String position = extra.getString("position");

        itemList = new ArrayList<>();
        groupAdapter = new GroupAdapter(this, itemList, position);
        groupView.setAdapter(groupAdapter);

        itemCost.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);

        purchasedReference.addValueEventListener(new ValueEventListener() {
            /**
             * Listens for changes on firebase database.
             * @param dataSnapshot The current data at the location
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchased item = snapshot.getValue(Purchased.class);
                    if (item.getItemList() != null && item.getId().equals(position)) {
                        for (int i = 0; i < item.getItemList().size(); i++) {
                            itemList.add(item.getItemList().get(i));
                        }
                    }
                }
                groupAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton3);
        backToUser.setOnClickListener(new View.OnClickListener() {
            /**
             * On click listener to return to the PurchasedActivity with the list of purchased lists.
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, PurchasedActivity.class);
                startActivity(intent);
            }
        });
    }
}