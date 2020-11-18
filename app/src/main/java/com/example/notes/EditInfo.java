package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.model.Note;
import com.example.notes.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.notes.Home.USERS;
import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;
import static com.example.notes.Profile.PHONE_KEY;
import static com.example.notes.Profile.USERNAME_KEY;

public class EditInfo extends AppCompatActivity {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS);

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Intent profileIntent = getIntent();
        final String username = profileIntent.getStringExtra(USERNAME_KEY);
        final String phone = profileIntent.getStringExtra(PHONE_KEY);
        final String email = profileIntent.getStringExtra(EMAIL_KEY);
        final String password = profileIntent.getStringExtra(PASSWORD_KEY);

        final EditText etUsername = (EditText)findViewById(R.id.etUsername);
        final EditText etPhone = (EditText)findViewById(R.id.etPhone);
        Button btnChangeInfo = (Button)findViewById(R.id.btnChangeInfo);

        etUsername.setText(username);
        etPhone.setText(phone);

        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userObj = new  User(email, password ,etUsername.getText().toString(), etPhone.getText().toString());
                ref.child(user.getUid()).setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditInfo.this, "Update Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra(EMAIL_KEY, email);
                        startActivity(intent);
                    }
                });
            }
        });


    }
}