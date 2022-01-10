package com.example.rejoylibrary;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class WalletRecord {
    private String userEmail, number, trxId, dbKey;
    private long time;
    private int amount, status;

    public WalletRecord(String userEmail, String number, String trxId, int amount, long time, int status) {
        this.userEmail = userEmail;
        this.number = number;
        this.trxId = trxId;
        this.amount = amount;
        this.time = time;
        this.status = status;
    }

    public static WalletRecord build(DataSnapshot snapshot) {
        String userEmail = snapshot.child("userEmail").getValue().toString();
        String number = snapshot.child("number").getValue().toString();
        String trxId = snapshot.child("trxId").getValue().toString();
        long time = Long.parseLong(snapshot.child("time").getValue().toString());
        int amount = Integer.parseInt(snapshot.child("amount").getValue().toString());
        int status = Integer.parseInt(snapshot.child("status").getValue().toString());

        WalletRecord walletRecord = new WalletRecord(userEmail, number, trxId, amount, time, status);
        walletRecord.setDbKey(snapshot.getKey());

        return walletRecord;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("userEmail", getUserEmail());
        mp.put("number", getNumber());
        mp.put("trxId", getTrxId());
        mp.put("amount", String.valueOf(getAmount()));
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
