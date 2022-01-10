package com.example.rejoylibrary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FragmentBookTabGet extends Fragment {
    private final int MAX_RENTED_BOOKS = 4;

    private BookRecord bookRecord;
    private TextView txtAvailable, txtBuyPrice, txtRentPrice;
    private Button btnBuy, btnRent, btnAdd;
    private User user;

    public FragmentBookTabGet(User user, BookRecord bookRecord) {
        super();
        this.user = user;
        this.bookRecord = bookRecord;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_tab_get, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Book book = bookRecord.getBook();
        int count = bookRecord.getCopiesAvailable();

        txtAvailable = view.findViewById(R.id.txtAvailable);
        txtAvailable.setText("Available: " + String.valueOf(bookRecord.getCopiesAvailable()));

        txtBuyPrice = view.findViewById(R.id.txtBuyPrice);
        txtBuyPrice.setText("Price: " + String.format("%.2f", bookRecord.getPriceBuy()) + " BDT");

        txtRentPrice = view.findViewById(R.id.txtRentPrice);
        txtRentPrice.setText("Price: " + String.format("%.2f", bookRecord.getPriceRentPerWeek()) + " BDT");

        btnBuy = view.findViewById(R.id.btnBuy);

        boolean disable;

        disable = false;
        for (BookBuyRecord record : Data.getBookBuyRecords().values()) {
            if (record.getBookName().equals(book.getName()) && record.getAuthorName().equals(book.getAuthor().getName())) {
                disable = true;
                break;
            }
        }
        disable = false;

        if (
            (disable) ||
            (count <= 0) ||
            (user.getBalance() < bookRecord.getPriceBuy()) ||
            (user.isSubExpired())
        ) btnBuy.setEnabled(false);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Buy " + book.getName() + "?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Update Balance
                                double currBalance = user.getBalance() - bookRecord.getPriceBuy();
                                if (currBalance >= 0.) {
                                    user.setBalance(currBalance);
                                    user.updateAtDB();

                                    BookBuyRecord record = new BookBuyRecord(user.getEmail(), book.getName(), book.getAuthor().getName(), (new Date()).getTime(), STATUS.PROCESSING);
                                    FirebaseDatabase.getInstance().getReference().child("bookBuyRecords").push().setValue(record.toHashMap());
                                    Data.addBookBuyRecord(record);

                                    bookRecord.setCopiesAvailable(count-1);
                                    bookRecord.updateAtDB();

                                    btnBuy.setEnabled(false);
                                }

                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        disable = (user.getInventoryRented().size() >= MAX_RENTED_BOOKS);
        for (BookRentRecord record : Data.getBookRentRecords().values()) {
            if (record.getBookKey().equals(book.getDbKey())) {
                disable = false;
                break;
            }
        }

        btnRent = view.findViewById(R.id.btnRent);
        if (
            (disable) ||
            (count <= 0) ||
            (user.getBalance() < bookRecord.getPriceRentPerWeek()) ||
            (user.isSubExpired())
        ) btnRent.setEnabled(false);
        btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Rent " + book.getName() + " for 1 week?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Update Balance
                                double currBalance = user.getBalance() - bookRecord.getPriceRentPerWeek();
                                if (currBalance >= 0.) {
                                    long currTime = (new Date()).getTime();
                                    long dt = 7*24*60*60*1000;
                                    BookRentRecord record = new BookRentRecord(user.getEmail(), book.getDbKey(), currTime, currTime+dt, STATUS.PROCESSING);

                                    // Check if the record exists already
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    BookRentRecord curr = null;
                                    for (String key : user.getInventoryRented()) {
                                        BookRentRecord rec = Data.getBookRentRecord(key);
                                        if (rec.getBookKey().equals(book.getDbKey())) {
                                            if (rec.getEndTime() > currTime) {
                                                curr = rec;
                                                Log.v("MainActivity", book.getName() + " FOUND");
                                                break;
                                            }
                                        }
                                    }

                                    if (curr != null) {
                                        String key = curr.getDbKey();
                                        long endTime = curr.getEndTime()+dt;
                                        Data.getBookRentRecord(key).setEndTime(endTime);
                                        curr.setEndTime(endTime);
                                        ref.child("bookRentRecords").child(key).setValue(curr.toHashMap());
                                    } else {
                                        String key = ref.child("bookRentRecords").push().getKey();
                                        ref.child("bookRentRecords").child(key).setValue(record.toHashMap());
                                        Data.addBookRentRecord(record);
                                        user.getInventoryRented().add(key);
                                        bookRecord.setCopiesAvailable(count-1);
                                        bookRecord.updateAtDB();
                                    }
                                    user.setBalance(currBalance);
                                    user.updateAtDB();
                                }

                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnAdd = view.findViewById(R.id.btnAdd);
        if (user.getWishlist().contains(book.getDbKey())) {
            btnAdd.setEnabled(false);
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.addToWishlist(book.getDbKey());
            }
        });
    }
}
