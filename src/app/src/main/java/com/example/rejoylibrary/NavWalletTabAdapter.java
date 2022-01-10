package com.example.rejoylibrary;

import android.content.Context;
import android.icu.text.AlphabeticIndex;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class NavWalletTabAdapter extends FragmentPagerAdapter {
    private User user;
    private int count;

    public NavWalletTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public NavWalletTabAdapter(Context context, User user, int count, @NonNull FragmentManager fm) {
        this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.user = user;
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentWalletAddMoney(user);
            case 1:
                return new FragmentWalletHistory();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
