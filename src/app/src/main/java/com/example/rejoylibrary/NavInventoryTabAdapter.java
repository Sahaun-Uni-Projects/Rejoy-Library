package com.example.rejoylibrary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rejoylibrary.databinding.FragmentNavInventoryBoughtBinding;

public class NavInventoryTabAdapter extends FragmentPagerAdapter {
    private User user;
    private int count;

    public NavInventoryTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public NavInventoryTabAdapter(Context context, User user, int count, @NonNull FragmentManager fm) {
        this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.user = user;
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentInventoryBought();
            case 1:
                return new FragmentInventoryRented();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
