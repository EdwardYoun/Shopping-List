package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PurchasedActivity extends AppCompatActivity {

    private ListView purchasedList;
    private DatabaseReference purchasedReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");


    }
}