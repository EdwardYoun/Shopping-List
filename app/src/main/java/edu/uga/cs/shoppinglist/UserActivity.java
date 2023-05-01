package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "UserActivity";
    private TextView signedInTextView;
    private DatabaseReference purchasedReference;
    public ArrayList<Purchased> purchasedList;

    /**
     * Sets up the UserActivity screen. This is the home page when a user is logged in.
     * Displays the current user and Shopping List, Recently Purchased, Settle the Cost, and Logout buttons.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        signedInTextView = findViewById( R.id.textView3 );
        Button shoppingListButton = (Button) findViewById(R.id.button9);
        Button logoutButton = (Button) findViewById(R.id.button8);
        Button recentlyPurchasedButton = (Button) findViewById(R.id.button6);
        Button settleCostButton = (Button) findViewById(R.id.button7);
        purchasedList = new ArrayList<>();
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        // Setup a listener for a change in the sign in status (authentication status change)
        // when it is invoked, check if a user is signed in and update the UI text view string,
        // as needed.
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    // User is signed in
                    Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Signed in as: \n" + userEmail );
                } else {
                    // User is signed out
                    shoppingListButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.GONE);
                    recentlyPurchasedButton.setVisibility(View.GONE);
                    settleCostButton.setVisibility(View.GONE);
                    Log.d( DEBUG_TAG, "onAuthStateChanged:signed_out" );
                    signedInTextView.setText( "Signed in as: not signed in" );
                }
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        });
        recentlyPurchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, PurchasedActivity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        settleCostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (purchasedList.size() == 0) {
                    Toast.makeText(view.getContext(),
                            "Nothing was purchased yet!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(UserActivity.this, SettleActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}