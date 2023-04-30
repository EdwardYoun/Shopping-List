package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EmailAdapter extends BaseAdapter {

    private static final String DEBUG_TAG = "EmailAdapter";

    private Context context;
    private ArrayList<Email> userList;
    private ArrayList<Purchased> purchasedList;
    private DatabaseReference userReference, purchasedReference;

    /**
     * Adapter for the email used to purchase items.
     * @param context Context of app.
     * @param userList List of users
     * @param purchasedList List of purchased items
     */
    public EmailAdapter(Context context, ArrayList<Email> userList, ArrayList<Purchased> purchasedList) {
        this.context = context;
        this.userList = userList;
        this.purchasedList = purchasedList;
    }

    /**
     * @return number of users
     */
    @Override
    public int getCount() { return userList.size(); }

    /**
     * @param position position in the list
     * @return item at the given position
     */
    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Gets view and populates the Settle Cost List View.
     * @param position position of the item to populate
     * @param convertView ListView of users
     * @param parent Parent View
     * @return The list of users and how much each spent. Also the average amount that each roommate needs
     * to pay based on the total spent by all roommates.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_settle, parent, false);
        }

        TextView settleView = convertView.findViewById(R.id.settleCost);
        TextView totalView = convertView.findViewById(R.id.totalCost);
        ImageButton goBackButton = convertView.findViewById(R.id.goBackButton4);
        userReference = FirebaseDatabase.getInstance().getReference("users");
        purchasedReference = FirebaseDatabase.getInstance().getReference("purchased");

        double total = 0;

        settleView.setVisibility(View.GONE);
        goBackButton.setVisibility(View.GONE);

        for (int i = 0; i < purchasedList.size(); i++) {
            if (userList.get(position).getEmail().equals(purchasedList.get(i).getUser())) {
                total = total + purchasedList.get(i).getTotal();
            }
        }

        if (position == userList.size()-1){
            double average = 0;
            for (int i = 0; i < purchasedList.size(); i++) {
                average = average + purchasedList.get(i).getTotal();
                Log.d(DEBUG_TAG, Integer.toString(i) + " " + String.format("%.2f", average));
            }
            average = (average / userList.size());
            Log.d(DEBUG_TAG, String.format("%.2f", average));
            totalView.setText(userList.get(position).getEmail() + ": $" + String.format("%.2f", total) + "\n\naverage cost per roommate: $" + String.format("%.2f", average));
        }
        else {
            totalView.setText(userList.get(position).getEmail() + ": $" + String.format("%.2f", total));
        }

        return convertView;
    }

}