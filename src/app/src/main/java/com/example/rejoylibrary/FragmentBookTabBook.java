package com.example.rejoylibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBookTabBook extends Fragment {
    private BookRecord bookRecord;
    private TextView txtBook;

    public FragmentBookTabBook(BookRecord bookRecord) {
        super();
        this.bookRecord = bookRecord;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_tab_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Book book = bookRecord.getBook();
        txtBook = view.findViewById(R.id.txtBook);
        txtBook.setText(book.getDescription());
    }
}
