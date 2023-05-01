package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettleActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "SettleActivity";

    private ListView settleView;
    private TextView totalView, averageView;
    private DatabaseReference userReference, purchasedReference;
    private ArrayList<Email> userList;
    private ArrayList<Purchased> purchasedList;
    private EmailAdapter emailAdapter;

    /**
     * Sets up the SettleActivity where the cost of the purchased items is divided among the users.
     * Listens for the data changes across the app and then clears the purchased list after settling.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        settleView = findViewById(R.id.userList1);
        totalView = findViewById(R.id.totalCost);

        userList = new ArrayList<>();
        purchasedList = new ArrayList<>();
        emailAdapter = new EmailAdapter(this, userList, purchasedList);
        settleView.setAdapter(emailAdapter);

        userReference = FirebaseDatabase.getInstance().getReference("users");
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        totalView.setVisibility(View.GONE);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Email user = snapshot.getValue(Email.class);
                    userList.add(user);
                }
                emailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        purchasedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchased item = snapshot.getValue(Purchased.class);
                    purchasedList.add(item);
                }
                emailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        //sends user back to user activity
        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton4);
        backToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchasedReference.removeValue();
                Intent intent = new Intent(SettleActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        purchasedReference.removeValue();
        Intent intent = new Intent(SettleActivity.this, UserActivity.class);
        startActivity(intent);
    }

}