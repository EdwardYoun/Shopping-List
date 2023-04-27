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
    private TextView textView;
    private Button removeButton;
    private DatabaseReference purchasedReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");
        groupView = findViewById(R.id.groupList);
        textView = findViewById(R.id.textView10);
        removeButton = findViewById(R.id.button16);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String position = extra.getString("position");

        itemList = new ArrayList<>();
        groupAdapter = new GroupAdapter(this, itemList, position);
        groupView.setAdapter(groupAdapter);

        textView.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);

        purchasedReference.addValueEventListener(new ValueEventListener() {
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
    }
}