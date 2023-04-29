package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EmailAdapter extends BaseAdapter {

    private static final String DEBUG_TAG = "EmailAdapter";

    private Context context;
    private ArrayList<Email> userList;
    private DatabaseReference userReference;

    public EmailAdapter(Context context, ArrayList<Email> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() { return userList.size(); }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_settle, parent, false);
        }

        TextView settleView = convertView.findViewById(R.id.settleCost);
        TextView totalView = convertView.findViewById(R.id.totalCost);
        ImageButton goBackButton = convertView.findViewById(R.id.goBackButton4);

        settleView.setVisibility(View.GONE);
        goBackButton.setVisibility(View.GONE);

        totalView.setText(userList.get(position).getEmail());

        return convertView;
    }

}