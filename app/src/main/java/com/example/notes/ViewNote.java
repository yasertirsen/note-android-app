package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.notes.AddNote.NOTES;
import static com.example.notes.Home.USERS;
import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.TAG_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.TEXT_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.TITLE_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.UID_KEY;
import static com.example.notes.Profile.PHONE_KEY;
import static com.example.notes.Profile.USERNAME_KEY;

public class ViewNote extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvTag;

    private String title;
    private String userUid;
    private String email;

    private FirebaseDatabase db;
    private DatabaseReference notesRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        Intent rcvIntent = getIntent();
        userUid = rcvIntent.getStringExtra(UID_KEY);
        title = rcvIntent.getStringExtra(TITLE_KEY);
        String tag = rcvIntent.getStringExtra(TAG_KEY);
        String text = rcvIntent.getStringExtra(TEXT_KEY);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvContent = (TextView)findViewById(R.id.tvContent);
        tvTag = (TextView)findViewById(R.id.tvTag);

        tvTitle.setText(title);
        tvContent.setText(text);
        tvTag.setText(tag);

        db = FirebaseDatabase.getInstance();
        notesRef = db.getReference(NOTES);
        usersRef = db.getReference(USERS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id==R.id.action_delete) {
            notesRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()) {
                        if(ds.getKey() != null && Objects.equals(ds.child("uid").getValue(), userUid)
                        && Objects.equals(ds.child("title").getValue(), title)) {
                            ds.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()) {
                        if(ds.getKey() != null && Objects.equals(ds.getKey(), userUid)) {
                            email = Objects.requireNonNull(ds.child("email").getValue()).toString();
                            startIntent();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Toast.makeText(getApplicationContext(), "Note has been deleted", Toast.LENGTH_SHORT).show();

            return true;
        }
        return true;
    }

    private void startIntent() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra(EMAIL_KEY, email);
        startActivity(intent);
    }
}