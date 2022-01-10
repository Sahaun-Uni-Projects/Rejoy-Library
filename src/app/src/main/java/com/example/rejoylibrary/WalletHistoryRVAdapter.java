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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WalletHistoryRVAdapter extends RecyclerView.Adapter<WalletHistoryRVAdapter.ViewHolder> {
    private final String TAG = "WalletHistoryRVAdapter";

    private View view;
    private Context context;
    private FragmentWalletHistory fragment;
    private ArrayList<WalletRecord> records;

    public WalletHistoryRVAdapter(Context context, FragmentWalletHistory fragment, ArrayList<WalletRecord> records) {
        this.context = context;
        this.fragment = fragment;
        this.records = records;
    }

    @NonNull
    @Override
    public WalletHistoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wallet_list_item, parent, false);
        ViewHolder holder = new ViewHolder(this.view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WalletHistoryRVAdapter.ViewHolder holder, int position) {
        WalletRecord record = this.records.get(position);

        int status = record.getStatus();
        holder.imgStatus.setImageResource(STATUS.getImage(status));
        holder.txtAmount.setText(String.format("%.2f", (double)record.getAmount()) + " BDT");
        holder.txtTrxId.setText(record.getTrxId());

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
        private TextView txtAmount, txtTrxId, txtDate, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgStatus = itemView.findViewById(R.id.imgStatus);
            this.txtAmount = itemView.findViewById(R.id.txtAmount);
            this.txtTrxId = itemView.findViewById(R.id.txtTrxId);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
