package com.example.rejoylibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookWishlistRVAdapter extends RecyclerView.Adapter<BookWishlistRVAdapter.ViewHolder> {
    private final String TAG = "BookRVAdapter";

    private View view;
    private Context context;
    private FragmentWishlist fragment;
    private ArrayList<Book> books;

    public BookWishlistRVAdapter(Context context, FragmentWishlist fragment, ArrayList<Book> books) {
        this.context = context;
        this.fragment = fragment;
        this.books = books;
    }

    @NonNull
    @Override
    public BookWishlistRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_wishlist_list_item, parent, false);
        ViewHolder holder = new ViewHolder(this.view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookWishlistRVAdapter.ViewHolder holder, int position) {
        Book book = this.books.get(position);

        holder.txtBookName.setText(book.getName());
        holder.txtAuthorName.setText(book.getAuthor().getName());
        holder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the book record and move to BookActivity
                FirebaseDatabase
                    .getInstance().getReference().child("bookRecords")
                    .orderByChild("bookKey").equalTo(book.getDbKey())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) gotoBookActivity(snapshot); }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.removeIndex(holder.getAdapterPosition());
            }
        });
    }

    private void gotoBookActivity(DataSnapshot snapshot) {
        BookRecord bookRecord = BookRecord.build(snapshot);
        Intent intent = new Intent(context, BookActivity.class);
        intent.putExtra("bookRecord", bookRecord);
        this.view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cview;
        private TextView txtBookName, txtAuthorName;
        private ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cview = itemView.findViewById(R.id.parent);
            this.txtBookName = itemView.findViewById(R.id.txtBookName);
            this.txtAuthorName = itemView.findViewById(R.id.txtAuthorName);
            this.btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
