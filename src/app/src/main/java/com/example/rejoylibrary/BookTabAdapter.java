package com.example.rejoylibrary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BookTabAdapter extends FragmentPagerAdapter {
    private Context context;
    private BookRecord bookRecord;
    private User user;
    private int count;

    public BookTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public BookTabAdapter(Context context, User user, BookRecord bookRecord, int count, @NonNull FragmentManager fm) {
        this(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.user = user;
        this.bookRecord = bookRecord;
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentBookTabGet(user, bookRecord);
            case 1:
                return new FragmentBookTabBook(bookRecord);
            case 2:
                return new FragmentBookTabAuthor(bookRecord);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
