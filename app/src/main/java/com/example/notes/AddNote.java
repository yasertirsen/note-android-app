package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.notes.Home.USERS;
import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.UID_KEY;
import static com.example.notes.Profile.PHONE_KEY;
import static com.example.notes.Profile.USERNAME_KEY;

public class AddNote extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private EditText etTag;
    private FloatingActionButton fabSave;

    private String email;
    private String username;
    private String phone;
    private String password;
    private String userUid;

    public static String NOTES = "notes";

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(USERS);
    private DatabaseReference noteRef = FirebaseDatabase.getInstance().getReference(NOTES);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Intent homeIntent = getIntent();
        email = homeIntent.getStringExtra(EMAIL_KEY);
        username = homeIntent.getStringExtra(USERNAME_KEY);
        password = homeIntent.getStringExtra(PASSWORD_KEY);
        phone = homeIntent.getStringExtra(PHONE_KEY);
        userUid = homeIntent.getStringExtra(UID_KEY);

        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);
        etTag = (EditText)findViewById(R.id.etTag);
        fabSave = (FloatingActionButton)findViewById(R.id.fabSave);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTitle.getText().toString().isEmpty() ||
                        etContent.getText().toString().isEmpty() ||
                        etTag.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all information", Toast.LENGTH_SHORT).show();
                }
                else
                    addNote();
            }
        });

    }

    public void addNote() {
        userRef.child(userUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Note note = new Note(userUid, etTitle.getText().toString(),
                                etTag.getText().toString(), etContent.getText().toString());
                        noteRef.push().setValue(note);

                        Toast.makeText(getApplicationContext(), "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra(EMAIL_KEY, email);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}