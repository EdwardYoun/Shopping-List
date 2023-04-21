package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.button2);
        Button login = findViewById(R.id.button3);

        register.setOnClickListener( new RegisterButtonClickListener() );
        login.setOnClickListener( new LoginButtonClickListener() );
    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            v.getContext().startActivity(intent);
        }
    }

    private class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}