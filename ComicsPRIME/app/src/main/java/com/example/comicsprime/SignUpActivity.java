package com.example.comicsprime;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comicsprime.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends Fragment {

    private static final String TAG = "SignUpActivity";

    EditText editemail, editusername, editpassword, editrollno, editphone;
    Button btnsignup;

    //FIREBASE

    FirebaseDatabase database;
    DatabaseReference users;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);


        //EDITS

        editemail = view.findViewById(R.id.email);
        editusername = view.findViewById(R.id.username);
        editpassword = view.findViewById(R.id.password);
        editphone = view.findViewById(R.id.phone);

        btnsignup = (Button) view.findViewById(R.id.signup);

        //TO ENABLE BUTTON AFTER TEXT IS WRITTEN

        editemail.addTextChangedListener(loginTextWatch);
        editusername.addTextChangedListener(loginTextWatch);
        editpassword.addTextChangedListener(loginTextWatch);
        editphone.addTextChangedListener(loginTextWatch);


        //FIREBASE

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final User user = new User(editemail.getText().toString(),
                        editusername.getText().toString(),
                        editpassword.getText().toString(),
                        editphone.getText().toString());



                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUsername()).exists()) {
                            Toast.makeText(getActivity(), "Already Registered", Toast.LENGTH_LONG).show();
                        } else{
                            users.child(user.getUsername()).setValue(user);
                            Toast.makeText(getActivity(), "Registered Successfully !", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //NULL
                    }
                });


            }
        });


        return view;
    }

    //TO ENABLE BUTTON AFTER TEXT IS WRITTEN

    private TextWatcher loginTextWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String emailInput = editemail.getText().toString().trim();
            String usernameInput = editusername.getText().toString().trim();
            String passwordInput = editpassword.getText().toString().trim();
            String phoneInput = editphone.getText().toString().trim();

            btnsignup.setEnabled(!emailInput.isEmpty()
                    && !usernameInput.isEmpty()
                    && !passwordInput.isEmpty()
                    && !phoneInput.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}