package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String DEBUG_TAG = "SettleActivity";

    private ListView settleView;
    private TextView totalView;
    private DatabaseReference userReference;
    private ArrayList<Email> userList;
    private EmailAdapter emailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        settleView = findViewById(R.id.userList);
        totalView = findViewById(R.id.totalCost);

        userList = new ArrayList<>();
        emailAdapter = new EmailAdapter(this, userList);
        settleView.setAdapter(emailAdapter);

        userReference = FirebaseDatabase.getInstance().getReference("users");

        totalView.setVisibility(View.GONE);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Email user = snapshot.getValue(Email.class);
                        userList.add(user);
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