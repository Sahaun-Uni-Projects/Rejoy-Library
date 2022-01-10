package com.example.rejoylibrary;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class BookRecord implements Serializable {
    private Book book;
    private Double priceBuy, priceRentPerWeek;
    private int copiesAvailable;
    private String dbKey;

    public BookRecord(Book book, int copiesAvailable, Double priceBuy, Double priceRentPerWeek) {
        this.book = book;
        this.copiesAvailable = copiesAvailable;
        this.priceBuy = priceBuy;
        this.priceRentPerWeek = priceRentPerWeek;
    }

    public static BookRecord build(DataSnapshot snapshot) {
        String bookKey = snapshot.child("bookKey").getValue().toString();
        Book book = Data.getBook(bookKey);
        int copiesAvailable = Integer.parseInt(snapshot.child("copiesAvailable").getValue().toString());
        Double priceBuy = Double.parseDouble(snapshot.child("priceBuy").getValue().toString());
        Double priceRentPerWeek = Double.parseDouble(snapshot.child("priceRentPerWeek").getValue().toString());

        BookRecord bookRecord = new BookRecord(book, copiesAvailable, priceBuy, priceRentPerWeek);
        bookRecord.setDbKey(snapshot.getKey());

        return bookRecord;
    }

    public void updateAtDB() {
        FirebaseDatabase.getInstance().getReference()
                .child("bookRecords").child(getDbKey()).setValue(toHashmap());
    }

    public HashMap<String, String> toHashmap() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("bookKey", book.getDbKey());
        mp.put("copiesAvailable", String.valueOf(getCopiesAvailable()));
        mp.put("priceBuy", String.valueOf(getPriceBuy()));
        mp.put("priceRentPerWeek", String.valueOf(getPriceRentPerWeek()));

        return mp;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public Double getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(Double priceBuy) {
        this.priceBuy = priceBuy;
    }

    public Double getPriceRentPerWeek() {
        return priceRentPerWeek;
    }

    public void setPriceRentPerWeek(Double priceRentPerWeek) {
        this.priceRentPerWeek = priceRentPerWeek;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }
}
