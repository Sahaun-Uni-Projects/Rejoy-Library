package com.example.rejoylibrary;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
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

public class FragmentWishlist extends Fragment {
    private final String TAG = "FragmentWishlist";

    private User user;
    private ArrayList<Book> books;
    private RecyclerView rvBooks;
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
        loadUserFromAuth();
    }

    private void loadUserFromAuth() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users")
                .orderByChild("email").equalTo(currUser.getEmail())
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
        this.books = new ArrayList<>();
        loadComponents(view);
        loadBooks();
    }

    private void loadComponents(View view) {
        this.rvBooks = view.findViewById(R.id.rvBooks);
        this.rvBooks.setAdapter(new BookWishlistRVAdapter(getActivity(), this, books));
        this.rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadBooks() {
        FirebaseDatabase.getInstance().getReference().child("books")
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initLoadedBook(snapshot); }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initLoadedBook(snapshot); }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
    }

    private void initLoadedBook(DataSnapshot snapshot) {
        ArrayList<String> wishlist = (ArrayList<String>) user.getWishlist().clone();
        String key = snapshot.getKey();
        if (wishlist.contains(key)) {
            wishlist.remove(key);
            Book book = Book.build(snapshot);
            this.books.add(book);
            this.rvBooks.setAdapter(new BookWishlistRVAdapter(getActivity(), this, this.books));
        }
    }

    public void removeIndex(int index) {
        Book book = this.books.get(index);
        user.removeFromWishlist(book.getDbKey());
    }
}

// -MlAzfeA4ZGRDuPlJ64w, -MlAzfeA4ZGRDuPlJ64y, -MlAzfeBZ2EW1GBNwvsC