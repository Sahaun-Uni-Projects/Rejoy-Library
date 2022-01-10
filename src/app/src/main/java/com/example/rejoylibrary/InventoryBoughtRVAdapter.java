package com.example.rejoylibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InventoryBoughtRVAdapter extends RecyclerView.Adapter<InventoryBoughtRVAdapter.ViewHolder> {
    private final String TAG = "InventoryBoughtRVAdapter";

    private View view;
    private Context context;
    private FragmentInventoryBought fragment;
    private ArrayList<BookBuyRecord> records;

    public InventoryBoughtRVAdapter(Context context, FragmentInventoryBought fragment, ArrayList<BookBuyRecord> records) {
        this.context = context;
        this.fragment = fragment;
        this.records = records;
    }

    @NonNull
    @Override
    public InventoryBoughtRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_buy_list_item, parent, false);
        ViewHolder holder = new ViewHolder(this.view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryBoughtRVAdapter.ViewHolder holder, int position) {
        BookBuyRecord record = this.records.get(position);

        int status = record.getStatus();
        holder.imgStatus.setImageResource(STATUS.getImage(status));
        holder.txtBookName.setText(record.getBookName());
        holder.txtAuthorName.setText(record.getAuthorName());

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String str = "";
        str += "Time: ";
        str += formatter.format(new Date(record.getTime()));
        holder.txtDate.setText(str);

        holder.txtStatus.setText(STATUS.getString(status));
    }

    @Override
    public int getItemCount() {
        return this.records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgStatus;
        private TextView txtBookName, txtAuthorName, txtDate, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgStatus = itemView.findViewById(R.id.imgStatus);
            this.txtBookName = itemView.findViewById(R.id.txtBookName);
            this.txtAuthorName = itemView.findViewById(R.id.txtAuthorName);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
