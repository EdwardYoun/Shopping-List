package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SettleActivity extends AppCompatActivity {

    private ListView settleView;
    private TextView totalView;
    private DatabaseReference purchasedReference;
    private DatabaseReference userReference;
    private ArrayList<String> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        settleView = findViewById(R.id.userList);
        totalView = findViewById(R.id.totalCost);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        userReference = FirebaseDatabase.getInstance().getReference("users");

        String test = "test";
        userList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        settleView.setAdapter(adapter);


        totalView.setVisibility(View.GONE);


        purchasedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchased item = snapshot.getValue(Purchased.class);

                        for (int i = 0; i < item.getItemList().size(); i++) {
                            userList.add(item.getUser());
                        }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });







        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton4);
        backToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettleActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }

}