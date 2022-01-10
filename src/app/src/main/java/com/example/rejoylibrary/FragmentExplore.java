package com.example.rejoylibrary;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class FragmentExplore extends Fragment {
    private HashMap<String, Uri> bookImages = new HashMap<>();
    private ArrayList<BookRecord> bookRecords = new ArrayList<>();
    private RecyclerView rvBooks;
    private SearchView searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.searchBar = view.findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<BookRecord> records = new ArrayList<>();
                for (BookRecord record : bookRecords) {
                    if (record.getBook().getName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                        records.add(record);
                    }
                }
                rvBooks.setAdapter(new BookRVAdapter(getActivity(), records, bookImages));

                return false;
            }
        });

        this.rvBooks = view.findViewById(R.id.rvBooks);
        this.rvBooks.setAdapter(new BookRVAdapter(getActivity(), bookRecords, bookImages));
        this.rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadBookRecordsFromDB();
    }

    private void loadBookRecordsFromDB() {
        FirebaseDatabase.getInstance().getReference().child("bookRecords")
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        BookRecord record = BookRecord.build(snapshot);
                        bookRecords.add(record);
                        Book book = record.getBook();
                        StorageReference ref = FirebaseStorage.getInstance().getReference("book_covers/"+book.getCoverImage());
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                bookImages.put(book.getDbKey(), uri);
                                rvBooks.setAdapter(new BookRVAdapter(getActivity(), bookRecords, bookImages));
                            }
                        });

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        BookRecord changedRecord = BookRecord.build(snapshot);
                        for (BookRecord bookRecord : bookRecords) {
                            if (bookRecord.getBook().getDbKey().equals(changedRecord.getBook().getDbKey())) {
                                bookRecord.setCopiesAvailable(changedRecord.getCopiesAvailable());
                            }
                        }
                        rvBooks.setAdapter(new BookRVAdapter(getActivity(), bookRecords, bookImages));
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
    }
}
