package com.example.rejoylibrary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FragmentSubscription extends Fragment {
    private TextView txtBalance, txtSubStatus;
    private Button btnSubscribe1, btnSubscribe2, btnSubscribe3;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_subscription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtBalance = view.findViewById(R.id.txtBalance);
        txtSubStatus = view.findViewById(R.id.txtSubStatus);

        btnSubscribe1 = view.findViewById(R.id.btnSubscribe1);
        btnSubscribe1.setOnClickListener(getClickListener(1, 100.));

        btnSubscribe2 = view.findViewById(R.id.btnSubscribe2);
        btnSubscribe2.setOnClickListener(getClickListener(6, 550.));

        btnSubscribe3 = view.findViewById(R.id.btnSubscribe3);
        btnSubscribe3.setOnClickListener(getClickListener(12, 1000.));

        loadUserFromAuth();
    }

    private View.OnClickListener getClickListener(int months, double price) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Confirm subscription for " + String.valueOf(months) + " month(s)?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Update Balance
                                double currBalance = user.getBalance() - price;
                                if (currBalance >= 0.) {
                                    user.setBalance(currBalance);

                                    // Update Time
                                    long time = months;
                                    time = time*30*24*60*60;
                                    long currTime = user.getSubExpiry();
                                    if (user.isSubExpired()) currTime = (new Date()).getTime()/1000;
                                    user.setSubExpiry(currTime + time);

                                    // Update DB
                                    FirebaseDatabase.getInstance().getReference().child("users").child(user.getDbKey())
                                            .setValue(user.toHashmap());
                                }

                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        };
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
        Double balance = user.getBalance();

        // Subscription status
        String str = "Not subscribed";
        if (!user.isSubExpired()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            str = "Subscribed (Ends ";
            str += formatter.format(new Date(user.getSubExpiry()*1000));
            str += ")";
        }
        txtSubStatus.setText(str);

        // Set wallet
        txtBalance.setText(String.format("%.2f", balance));

        // Disable buttons
        btnSubscribe1.setEnabled(true);
        btnSubscribe2.setEnabled(true);
        btnSubscribe3.setEnabled(true);
        if (balance < 1000.) {
            btnSubscribe3.setEnabled(false);
            if (balance < 550.) {
                btnSubscribe2.setEnabled(false);
                if (balance < 100.) {
                    btnSubscribe1.setEnabled(false);
                }
            }
        }
    }
}
