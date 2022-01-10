package com.example.rejoylibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class BookActivity extends AppCompatActivity {
    private BookRecord bookRecord;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Intent
        Intent intent = getIntent();
        this.bookRecord = (BookRecord)intent.getSerializableExtra("bookRecord");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(bookRecord.getBook().getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize book
        Book book = bookRecord.getBook();
        setTitle(book.getName());


        ImageView imgCover = findViewById(R.id.bookCoverImage);
        StorageReference ref = FirebaseStorage.getInstance().getReference("book_covers/"+book.getCoverImage());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(BookActivity.this)
                        .load(uri.toString())
                        .into(imgCover);
            }
        });

        //int coverID = getResources().getIdentifier(book.getCoverImage(),"drawable", getPackageName());
        //imgCover.setImageResource(coverID);

        TextView bookName = findViewById(R.id.bookName);
        bookName.setText(book.getName());

        TextView bookAuthor = findViewById(R.id.bookAuthor);
        bookAuthor.setText(book.getAuthor().getName());

        // Init
        loadUserFromAuth();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUserFromAuth() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users")
            .orderByChild("email").equalTo(currUser.getEmail())
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { if (snapshot.exists()) initUser(snapshot); }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
    }

    private void initUser(DataSnapshot snapshot) {
        user = User.build(snapshot);

        // Tab Layout
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.removeAllTabs();

        tabLayout.addTab(tabLayout.newTab().setText("Get"));
        tabLayout.addTab(tabLayout.newTab().setText("Book"));
        tabLayout.addTab(tabLayout.newTab().setText("Author"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        BookTabAdapter adapter = new BookTabAdapter(this, user, bookRecord, tabLayout.getTabCount(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

}