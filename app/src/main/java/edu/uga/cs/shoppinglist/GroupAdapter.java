package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemList;
    private DatabaseReference purchasedReference;
    private String id;

    public GroupAdapter(Context context, ArrayList<Item> itemList, String id) {
        this.context = context;
        this.itemList = itemList;
        this.id = id;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_list_view, parent, false);
        }

        TextView itemView = convertView.findViewById(R.id.textView10);
        Button removeButton = convertView.findViewById(R.id.button16);
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        itemView.setText(itemList.get(position).getItemName());

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasedReference.child(id).child("itemList").child(Integer.toString(position)).removeValue();
            }
        });

        return convertView;
    }
}