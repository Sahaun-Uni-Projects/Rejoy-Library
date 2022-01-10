package com.example.rejoylibrary;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InvalidClassException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable {
    private String name, email, dbKey;
    private ArrayList<String> wishlist, inventoryBought, inventoryRented;
    private double balance;
    private long subExpiry;

    public User(String name, String email, double balance, long subExpiry) {
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.subExpiry = subExpiry;

        this.wishlist = new ArrayList<>();
        this.inventoryBought = new ArrayList<>();
        this.inventoryRented = new ArrayList<>();
    }

    public static User build(DataSnapshot snapshot) {
        String name  = snapshot.child("name").getValue().toString();
        String email = snapshot.child("email").getValue().toString();
        double balance = Double.valueOf(snapshot.child("balance").getValue().toString());
        long subExpiry = Integer.parseInt(snapshot.child("subExpiry").getValue().toString());

        String wishlist = snapshot.child("wishlist").getValue().toString();
        String inventoryBought = snapshot.child("inventoryBought").getValue().toString();
        String inventoryRented = snapshot.child("inventoryRented").getValue().toString();

        User user = new User(name, email, balance, subExpiry);
        user.setDbKey(snapshot.getKey());
        user.setWishlist(user.getArrayList(wishlist));
        user.setInventoryBought(user.getArrayList(inventoryBought));
        user.setInventoryRented(user.getArrayList(inventoryRented));

        return user;
    }

    private ArrayList<String> getArrayList(String str) {
        str = str.substring(1, str.length()-1);
        if (str.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(str.split(", ")));
    }

    public HashMap<String, String> toHashmap() {
        HashMap<String, String> mp = new HashMap<>();
        mp.put("balance", String.valueOf(getBalance()));
        mp.put("email", getEmail());
        mp.put("inventoryBought", getInventoryBought().toString());
        mp.put("inventoryRented", getInventoryRented().toString());
        mp.put("name", getName());
        mp.put("subExpiry", String.valueOf(getSubExpiry()));
        mp.put("wishlist", getWishlist().toString());

        return mp;
    }

    public boolean addToWishlist(String key, boolean updateDB) {
        if (!wishlist.contains(key)) {
            wishlist.add(key);
            if (updateDB) updateAtDB();
            return true;
        }
        return false;
    }
    public boolean addToWishlist(String key) {
        return addToWishlist(key, true);
    }

    public boolean removeFromWishlist(String key, boolean updateDB) {
        if (wishlist.contains(key)) {
            wishlist.remove(key);
            if (updateDB) updateAtDB();
            return true;
        }
        return false;
    }
    public boolean removeFromWishlist(String key) {
        return removeFromWishlist(key, true);
    }

    public boolean removeFromInventoryRented(String key, boolean updateDB) {
        if (inventoryRented.contains(key)) {
            inventoryRented.remove(key);
            if (updateDB) updateAtDB();
            return true;
        }
        return false;
    }
    public boolean removeFromInventoryRented(String key) {
        return removeFromInventoryRented(key, true);
    }

    public boolean addToInventoryRented(String key, boolean updateDB) {
        if (!inventoryRented.contains(key)) {
            inventoryRented.add(key);
            if (updateDB) updateAtDB();
            return true;
        }
        return false;
    }
    public boolean addToInventoryRented(String key) {
        return addToInventoryRented(key, true);
    }

    public boolean addToInventoryBought(String key, boolean updateDB) {
        if (!inventoryBought.contains(key)) {
            inventoryBought.add(key);
            if (updateDB) updateAtDB();
            return true;
        }
        return false;
    }
    public boolean addToInventoryBought(String key) {
        return addToInventoryBought(key, true);
    }

    public void updateAtDB() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(getDbKey()).setValue(toHashmap());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getSubExpiry() {
        return this.subExpiry;
    }

    public void setSubExpiry(long subExpiry) {
        this.subExpiry = subExpiry;
    }

    public ArrayList<String> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<String> wishlist) {
        this.wishlist = wishlist;
    }

    public ArrayList<String> getInventoryBought() {
        return inventoryBought;
    }

    public void setInventoryBought(ArrayList<String> inventoryBought) {
        this.inventoryBought = inventoryBought;
    }

    public ArrayList<String> getInventoryRented() {
        return inventoryRented;
    }

    public void setInventoryRented(ArrayList<String> inventoryRented) {
        this.inventoryRented = inventoryRented;
    }

    public String getDbKey() {
        return this.dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public boolean isSubExpired() {
        long currTime = Math.round((new Date()).getTime() / 1000);
        return (currTime >= this.subExpiry);
    }
}
