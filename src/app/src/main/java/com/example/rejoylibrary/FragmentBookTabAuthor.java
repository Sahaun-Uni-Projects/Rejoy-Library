package com.example.rejoylibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBookTabAuthor extends Fragment {
    private BookRecord bookRecord;
    private TextView txtAuthor;

    public FragmentBookTabAuthor(BookRecord bookRecord) {
        super();
        this.bookRecord = bookRecord;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_tab_author, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Author author = bookRecord.getBook().getAuthor();
        txtAuthor = view.findViewById(R.id.txtAuthor);
        txtAuthor.setText(author.getDescription());
    }
}
