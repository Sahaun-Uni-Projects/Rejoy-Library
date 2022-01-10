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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentInventoryRented extends Fragment {
    private final String TAG = "FragmentInventoryRented";

    private ArrayList<BookRentRecord> records = new ArrayList<>();
    private RecyclerView rvRecords;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_inventory_rented, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        loadRecords();
    }

    private void loadRecords() {
        FirebaseDatabase.getInstance().getReference().child("bookRentRecords")
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
        BookRentRecord record = BookRentRecord.build(snapshot);
        BookRentRecord delRecord = null;
        for (BookRentRecord rec : records) {
            if (rec.getDbKey().equals(record.getDbKey())) {
                delRecord = rec;
                break;
            }
        }
        if (delRecord != null) records.remove(delRecord);
        records.add(record);

        loadComponents();
    }

    private void loadComponents() {
        this.rvRecords = view.findViewById(R.id.rvRecords);
        this.rvRecords.setAdapter(new InventoryRentedRVAdapter(getActivity(), this, records));
        this.rvRecords.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
