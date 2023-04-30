package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchasedActivity extends AppCompatActivity {

    private ArrayList<Purchased> purchasedList;
    private ListView purchasedView;
    private PurchasedAdapter purchasedAdapter;
    private DatabaseReference purchasedReference;
    private TextView itemsView, userView, dollarSign;
    private EditText totalView;
    private ImageButton priceButton, editButton;

    /**
     * Sets up the purchased activity.
     * Layout for list of purchased item groups.
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");
        purchasedView = findViewById(R.id.purchasedItems);
        itemsView = findViewById(R.id.textView7);
        totalView = findViewById(R.id.textView8);
        userView = findViewById(R.id.textView9);
        priceButton = findViewById(R.id.button14);
        editButton = findViewById(R.id.button15);
        dollarSign =findViewById(R.id.textView11);

        itemsView.setVisibility(View.GONE);
        totalView.setVisibility(View.GONE);
        userView.setVisibility(View.GONE);
        priceButton.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);
        dollarSign.setVisibility(View.GONE);

        purchasedList = new ArrayList<>();
        purchasedAdapter = new PurchasedAdapter(this, purchasedList);
        purchasedView.setAdapter(purchasedAdapter);

        purchasedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchased item = snapshot.getValue(Purchased.class);
                    purchasedList.add(item);
                }
                purchasedAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        ImageButton backToUser = (ImageButton) findViewById(R.id.goBackButton2);
        backToUser.setOnClickListener(new View.OnClickListener() {
            /**
             * OnClickListener to return to MainActivity
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchasedActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}