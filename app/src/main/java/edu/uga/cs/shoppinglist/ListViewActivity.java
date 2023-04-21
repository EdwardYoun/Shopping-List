package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {


    private EditText itemNameEditText, priceCostEditText;
    private Button addButton;
    private ListView listView;
    private ArrayList<Item> itemList;
    private ItemAdapter itemAdapter;
    private DatabaseReference databaseReference;


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
                String itemName = itemNameEditText.getText().toString().trim();
                String priceCost = priceCostEditText.getText().toString().trim();

                if (!itemName.isEmpty() && !priceCost.isEmpty()) {
                    String itemId = databaseReference.push().getKey();
                    Item item = new Item(itemId, itemName, priceCost);
                    databaseReference.child(itemId).setValue(item);
                    itemList.add(item);
                    itemNameEditText.setText("");
                    priceCostEditText.setText("");
                    itemAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}