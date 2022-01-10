package com.example.rejoylibrary;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BookRentRecord {
    private String userEmail, dbKey, bookKey;
    private long time, endTime;
    private int status;

    public BookRentRecord(String userEmail, String bookKey, long time, long endTime, int status) {
        this.userEmail = userEmail;
        this.time = time;
        this.endTime = endTime;
        this.bookKey = bookKey;
        this.status = status;
    }

    public static BookRentRecord build(DataSnapshot snapshot) {
        String userEmail = snapshot.child("userEmail").getValue().toString();
        String bookKey = snapshot.child("bookKey").getValue().toString();
        long time = Long.parseLong(snapshot.child("time").getValue().toString());
        long endTime = Long.parseLong(snapshot.child("endTime").getValue().toString());
        int status = Integer.parseInt(snapshot.child("status").getValue().toString());

        BookRentRecord brRecord = new BookRentRecord(userEmail, bookKey, time, endTime, status);
        brRecord.setDbKey(snapshot.getKey());

        return brRecord;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("userEmail", getUserEmail());
        mp.put("bookKey", getBookKey());
        mp.put("time", String.valueOf(getTime()));
        mp.put("endTime", String.valueOf(getEndTime()));
        mp.put("status", String.valueOf(getStatus()));

        return mp;
    }

    public void updateAtDB() {
        FirebaseDatabase.getInstance().getReference()
                .child("bookRentRecords").child(getDbKey()).setValue(toHashMap());
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookKey() {
        return bookKey;
    }

    public void setBookKey(String bookKey) {
        this.bookKey = bookKey;
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

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
