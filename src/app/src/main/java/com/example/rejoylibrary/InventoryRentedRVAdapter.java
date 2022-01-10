package com.example.rejoylibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InventoryRentedRVAdapter extends RecyclerView.Adapter<InventoryRentedRVAdapter.ViewHolder> {
    private final String TAG = "InventoryRentedRVAdapter";

    private View view;
    private Context context;
    private FragmentInventoryRented fragment;
    private ArrayList<BookRentRecord> records;

    public InventoryRentedRVAdapter(Context context, FragmentInventoryRented fragment, ArrayList<BookRentRecord> records) {
        this.context = context;
        this.fragment = fragment;
        this.records = records;
    }

    @NonNull
    @Override
    public InventoryRentedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_rent_list_item, parent, false);
        ViewHolder holder = new ViewHolder(this.view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryRentedRVAdapter.ViewHolder holder, int position) {
        BookRentRecord record = this.records.get(position);
        Book book = Data.getBook(record.getBookKey());

        int status = record.getStatus();
        holder.imgStatus.setImageResource(STATUS.getImage(status));
        holder.txtBookName.setText(book.getName());
        holder.txtAuthorName.setText(book.getAuthor().getName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        holder.txtDate.setText("Date: " + formatter.format(new Date(record.getTime())));
        holder.txtEndDate.setText("Date: " + formatter.format(new Date(record.getEndTime())));
        holder.txtStatus.setText(STATUS.getString(status));

        if ((new Date()).getTime() >= record.getEndTime()) {
            holder.parent.setCardBackgroundColor(view.getResources().getColor(R.color.c_red2));

            int white = view.getResources().getColor(R.color.c_white);
            holder.imgStatus.setImageResource(R.drawable.ic_baseline_warning_24);
            ImageViewCompat.setImageTintList(holder.imgStatus, ColorStateList.valueOf(white));

            holder.txtBookName.setTextColor(white);
            holder.txtAuthorName.setTextColor(white);
            holder.txtDate.setTextColor(white);
            holder.txtEndDate.setTextColor(white);

            holder.txtStatus.setTextColor(white);
            holder.txtStatus.setText("Expired");
        }
    }

    @Override
    public int getItemCount() {
        return this.records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private ImageView imgStatus;
        private TextView txtBookName, txtAuthorName, txtDate, txtEndDate, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.parent = itemView.findViewById(R.id.parent);
            this.imgStatus = itemView.findViewById(R.id.imgStatus);
            this.txtBookName = itemView.findViewById(R.id.txtBookName);
            this.txtAuthorName = itemView.findViewById(R.id.txtAuthorName);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtEndDate = itemView.findViewById(R.id.txtEndDate);
            this.txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
