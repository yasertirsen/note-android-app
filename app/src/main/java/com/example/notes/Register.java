package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes.model.Note;
import com.example.notes.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.notes.Home.USERS;


public class Register extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private EditText etPhone;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String email;
    private String password;
    private String username;
    private String phone;

    public DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPhone = (EditText)findViewById(R.id.etPhone);
        Button btnSignUp = (Button) findViewById(R.id.btnChangeInfo);

        db = FirebaseDatabase.getInstance().getReference(USERS);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

    }

    private void addUser() {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        username = etUsername.getText().toString();
        phone = etPhone.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username) || TextUtils.isEmpty(phone))
            Toast.makeText(this, "Please fill all info", Toast.LENGTH_SHORT).show();
        else {

            mAuth = FirebaseAuth.getInstance();
            //store user in Auth DB
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d("Register", "createUserWithEmail:success");
                                mUser = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Log.i("INFO", "User Profile Updated");
                                        }
                                    }
                                });
                                saveInRtDb();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Log.w("Register", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Register.this, "Authentication failure",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void saveInRtDb() {
        //store user in RT DB
        String id = db.push().getKey();

        User user = new User(email, password, username, phone);

        assert id != null;
        db.child(id).setValue(user);

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
    }
}