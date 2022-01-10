package com.example.rejoylibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BookRVAdapter extends RecyclerView.Adapter<BookRVAdapter.ViewHolder> {
    private final String TAG = "BookRVAdapter";

    private Context context;
    private HashMap<String, Uri> bookImages;
    private ArrayList<BookRecord> bookRecords;
    private View view;

    public BookRVAdapter(Context context, ArrayList<BookRecord> bookRecords, HashMap<String, Uri> bookImages) {
        this.context = context;
        this.bookRecords = bookRecords;
        this.bookImages = bookImages;
    }

    @NonNull
    @Override
    public BookRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookRVAdapter.ViewHolder holder, int position) {
        BookRecord bookRecord = this.bookRecords.get(position);
        Book book = bookRecord.getBook();

        Glide.with(view.getContext())
                .load(bookImages.get(book.getDbKey()))
                .into(holder.imgCover);

        holder.txtBookName.setText(book.getName());
        holder.txtWriterName.setText(book.getAuthor().getName());
        holder.txtAvailable.setText("Available : " + String.valueOf(bookRecord.getCopiesAvailable()));
        holder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("bookRecord", bookRecord);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.bookRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cview;
        private TextView txtBookName, txtWriterName, txtAvailable;
        private ImageView imgCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cview = itemView.findViewById(R.id.parent);
            this.txtBookName = itemView.findViewById(R.id.bookName);
            this.txtWriterName = itemView.findViewById(R.id.bookAuthor);
            this.imgCover = itemView.findViewById(R.id.bookCoverImage);
            this.txtAvailable = itemView.findViewById(R.id.bookCopiesAvailable);
        }
    }
}
