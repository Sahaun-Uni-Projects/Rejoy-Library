package com.example.rejoylibrary;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BookBuyRecord {
    private String userEmail, dbKey, bookName, authorName;
    private long time;
    private int status;

    public BookBuyRecord(String userEmail, String bookName, String authorName, long time, int status) {
        this.userEmail = userEmail;
        this.time = time;
        this.bookName = bookName;
        this.authorName = authorName;
        this.status = status;
    }

    public static BookBuyRecord build(DataSnapshot snapshot) {
        String userEmail = snapshot.child("userEmail").getValue().toString();
        String bookName = snapshot.child("bookName").getValue().toString();
        String authorName = snapshot.child("authorName").getValue().toString();
        long time = Long.parseLong(snapshot.child("time").getValue().toString());
        int status = Integer.parseInt(snapshot.child("status").getValue().toString());

        BookBuyRecord bbRecord = new BookBuyRecord(userEmail, bookName, authorName, time, status);
        bbRecord.setDbKey(snapshot.getKey());

        return bbRecord;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("userEmail", getUserEmail());
        mp.put("bookName", getBookName());
        mp.put("authorName", getAuthorName());
        mp.put("time", String.valueOf(getTime()));
        mp.put("status", String.valueOf(getStatus()));

        return mp;
    }

    public void updateAtDB() {
        FirebaseDatabase.getInstance().getReference()
                .child("bookBuyRecords").child(getDbKey()).setValue(toHashMap());
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
