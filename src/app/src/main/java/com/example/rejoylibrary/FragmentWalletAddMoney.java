package com.example.rejoylibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FragmentWalletAddMoney extends Fragment {
    private TextView txtBalance, txtError;
    private TextInputEditText inAmount, inNumber, inTrxId;
    private Button btnAdd;
    private User user;

    public FragmentWalletAddMoney(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_wallet_add_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtError   = view.findViewById(R.id.txtError);

        txtBalance = view.findViewById(R.id.txtBalance);
        txtBalance.setText(String.format("%.2f", user.getBalance()));

        inAmount   = view.findViewById(R.id.inAmount);
        inNumber   = view.findViewById(R.id.inNumber);
        inTrxId    = view.findViewById(R.id.inTrxId);
        btnAdd     = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = inAmount.getText().toString();
                String number = inNumber.getText().toString();
                String trxId  = inTrxId.getText().toString();

                if (amount.isEmpty() || number.isEmpty() || trxId.isEmpty()) {
                    showErrorMessage("Fields can not be empty.");
                    return;
                }

                int val = Integer.parseInt(amount);
                if (val < 20) {
                    showErrorMessage("Amount must be 20 BDT at minimum.");
                    return;
                }

                addTransactionToDB(val, number, trxId);
                resetComponents();
            }
        });
    }

    private void showErrorMessage(String msg) {
        txtError.setVisibility(View.VISIBLE);
        txtError.setText(msg);
    }

    private void resetComponents() {
        inAmount.setText("");
        inNumber.setText("");
        inTrxId.setText("");

        txtError.setVisibility(View.INVISIBLE);
    }

    private void addTransactionToDB(int amount, String number, String trxId) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        long time = (new Date()).getTime();
        WalletRecord record = new WalletRecord(email, number, trxId, amount, time, STATUS.PROCESSING);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("walletRecords");
        dbRef.push().setValue(record.toHashMap());
    }
}