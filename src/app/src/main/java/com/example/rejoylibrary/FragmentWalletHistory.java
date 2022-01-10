package com.example.rejoylibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentWalletHistory extends Fragment {
    private final String TAG = "FragmentWalletHistory";

    private ArrayList<WalletRecord> records = new ArrayList<>();
    private RecyclerView rvRecords;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        /*this.records = new ArrayList<>();
        for (WalletRecord record : Data.getWalletRecords().values()) {
            this.records.add(record);
        }*/

        loadRecords();
    }

    private void loadComponents() {
        this.rvRecords = view.findViewById(R.id.rvBooks);
        this.rvRecords.setAdapter(new WalletHistoryRVAdapter(getActivity(), this, records));
        this.rvRecords.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadRecords() {
        FirebaseDatabase.getInstance().getReference().child("walletRecords")
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) init_record(snapshot); }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) init_record(snapshot); }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
    }

    private void init_record(DataSnapshot snapshot) {
        WalletRecord record = WalletRecord.build(snapshot);
        WalletRecord delRecord = null;
        for (WalletRecord rec : records) {
            if (rec.getTrxId().equals(record.getTrxId())) {
                delRecord = rec;
                break;
            }
        }
        if (delRecord != null) records.remove(delRecord);
        records.add(record);

        loadComponents();
    }
}
