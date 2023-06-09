package edu.uga.cs.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "RegisterActivity";
    private EditText emailEditText;
    private EditText passwordEditText;
    private DatabaseReference userReference;

    /**
     * Sets up the registerActivity to register a new user.
     * Connects to the Firebase database "users" path.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById( R.id.editTextTextPersonName3 );
        passwordEditText = findViewById( R.id.editTextTextPersonName2 );
        userReference = FirebaseDatabase.getInstance().getReference("users");

        Button registerButton = findViewById( R.id.button4 );
        registerButton.setOnClickListener( new RegisterButtonClickListener() );
    }

    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String email = emailEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            if (email.equals("") && password.equals("")) {
                emailEditText.setText("Enter email!");
                passwordEditText.setText("Enter password!");
            }
            else if (email.equals("")) {
                emailEditText.setText("Enter email!");
            }
            else if (password.equals("")) {
                passwordEditText.setText("Enter password!");
            }
            else {
                // This is how we can create a new user using an email/password combination.
                // Note that we also add an onComplete listener, which will be invoked once
                // a new user has been created by Firebase.  This is how we will know the
                // new user creation succeeded or failed.
                // If a new user has been created, Firebase already signs in the new user;
                // no separate sign in is needed.
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(),
                                            "Registered user: " + email,
                                            Toast.LENGTH_SHORT).show();

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(DEBUG_TAG, "createUserWithEmail: success");

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String userId = userReference.push().getKey();
                                    userReference.child(userId).child("email").setValue(user.getEmail());

                                    Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(DEBUG_TAG, "createUserWithEmail: failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    if (error.equals("The email address is badly formatted.")) {
                                        emailEditText.setText("Invalid email format!");
                                        passwordEditText.setText("");
                                    }
                                    else if (error.equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                                        emailEditText.setText("");
                                        passwordEditText.setText("6 or more characters!");
                                    }
                                    else if (error.equals("The email address is already in use by another account.")) {
                                        emailEditText.setText("email already in use!");
                                        passwordEditText.setText("");
                                    } else {
                                        emailEditText.setText("");
                                        passwordEditText.setText("");
                                    }
                                }
                            }
                        });
            }
        }
    }
}