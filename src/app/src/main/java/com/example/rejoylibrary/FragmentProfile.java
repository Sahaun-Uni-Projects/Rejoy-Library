package com.example.rejoylibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FragmentProfile extends Fragment {
    private User user;
    private TextInputEditText inName, inEmail, inNewPassword, inNewPasswordRepeat;
    private TextView txtError;
    private Button btnUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inName = getActivity().findViewById(R.id.inName);
        inEmail = getActivity().findViewById(R.id.inEmail);
        inNewPassword = getActivity().findViewById(R.id.inNewPassword);
        inNewPasswordRepeat = getActivity().findViewById(R.id.inNewPasswordRepeat);
        txtError = getActivity().findViewById(R.id.txtError);
        btnUpdate = getActivity().findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inName.getText().toString();
                String npass = inNewPassword.getText().toString();
                String npass2 = inNewPasswordRepeat.getText().toString();

                if (name.isEmpty()) {
                    showErrorMessage("Name cannot be empty.");
                    return;
                }

                if ((!npass.isEmpty() || !npass2.isEmpty()) && !npass.equals(npass2)) {
                    showErrorMessage("Passwords do not match.");
                    return;
                }

                updateUser(name, npass);
            }
        });

        loadUserFromAuth();
    }

    private void loadUserFromAuth() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void initUser(DataSnapshot snapshot) {
        user = User.build(snapshot);
        resetComponents();
    }

    private void updateUser(String name, String password) {
        user.setName(name);
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getDbKey())
                .setValue(user.toHashmap());
        if (!password.isEmpty()) {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(password);
        }
        resetComponents();
    }

    private void showErrorMessage(String msg) {
        txtError.setVisibility(View.VISIBLE);
        txtError.setText(msg);
    }

    private void resetComponents() {
        // User info
        inName.setText(user.getName());
        inEmail.setText(user.getEmail());
        inNewPassword.setText("");
        inNewPasswordRepeat.setText("");

        // Error message
        txtError.setVisibility(View.INVISIBLE);
    }
}
