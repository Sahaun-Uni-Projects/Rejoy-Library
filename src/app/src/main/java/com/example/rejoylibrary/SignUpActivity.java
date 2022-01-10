package com.example.rejoylibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText inName, inEmail, inPassword, inPasswordRepeat;
    private TextView txtError;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sign Up");

        // Components
        inName = findViewById(R.id.inName);
        inEmail = findViewById(R.id.inEmail);
        inPassword = findViewById(R.id.inPassword);
        inPasswordRepeat = findViewById(R.id.inPasswordRepeat);
        txtError = findViewById(R.id.txtError);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name  = inName.getText().toString();
                String email = inEmail.getText().toString();
                String pass  = inPassword.getText().toString();
                String passR = inPasswordRepeat.getText().toString();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                    showErrorMessage("Credentials cannot be empty.");
                    return;
                }

                if (pass.length() < 6) {
                    showErrorMessage("Password must be at least 6 characters long.");
                    return;
                }

                if (!pass.equals(passR)) {
                    showErrorMessage("Passwords do not match.");
                    return;
                }

                signUp(name, email, pass);
            }
        });
    }

    private void showErrorMessage(String msg) {
        txtError.setVisibility(View.VISIBLE);
        txtError.setText(msg);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void signUp(String name, String email, String pass) {
        // Add to database
        User user = new User(name, email, 0., 0);
        FirebaseDatabase.getInstance().getReference().child("users").push().setValue(user.toHashmap());

        // Create a new user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        updateUI(currentUser.getEmail());
                    }
                }
            });
    }

    private void updateUI(String email) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}