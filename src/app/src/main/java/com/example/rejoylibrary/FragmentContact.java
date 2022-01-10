package com.example.rejoylibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FragmentContact extends Fragment {
    private final String TAG = "FragmentContact";

    private EditText etMessage;
    private Button btnSend;
    private TextView txtError;
    private AutoCompleteTextView ddMenu;

    private String[] options = {
        "Issue with adding money",
        "Issue with buying book(s)",
        "Issue with renting book(s)",
        "Bug with in-app functionality",
        "Review",
        "Other"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMessage = view.findViewById(R.id.etMessage);
        txtError = view.findViewById(R.id.txtError);

        btnSend  = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMessage.getText().toString();
                if (msg.isEmpty()) {
                    showErrorMessage("Message cannot be empty.");
                    return;
                }

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                HashMap<String, String> mp = new HashMap<>();
                mp.put("email", email);
                mp.put("message", msg);
                mp.put("time", String.valueOf((new Date()).getTime()));
                FirebaseDatabase.getInstance().getReference().child("contact").push().setValue(mp);

                resetComponents();
            }
        });

        ddMenu = view.findViewById(R.id.ddMenu);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_contact_ddmenu_item, options);
        ddMenu.setAdapter(adapter);
        ddMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ddMenu.setText(options[i], false);
                ddMenu.dismissDropDown();
            }
        });
    }

    private void resetComponents() {
        etMessage.setText("");
        hideErrorMessage();
    }

    private void showErrorMessage(String msg) {
        txtError.setVisibility(View.VISIBLE);
        txtError.setText(msg);
    }

    private void hideErrorMessage() {
        txtError.setVisibility(View.INVISIBLE);
    }

}
