package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

import static com.example.notes.AddNote.NOTES;
import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.UID_KEY;
import static com.example.notes.Profile.PHONE_KEY;
import static com.example.notes.Profile.USERNAME_KEY;

public class Home extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private DatabaseReference notesRef;

    public static final String USERS = "users";
    public static final String NOTES_KEY = "NOTES_KEY";

    private String email;
    private String username;
    private String phone;
    private String password;
    private String userUid;
    private Date date;

    private NotesAdapter mAdapter;

    private ArrayList<Note> notes = new ArrayList<>();

    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent loginIntent = getIntent();
        email = loginIntent.getStringExtra(EMAIL_KEY);

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference(USERS);
        notesRef = db.getReference(NOTES);

        userRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    if(ds.getKey() != null && Objects.equals(ds.child("email").getValue(), email)) {
                        try {
                            username = Objects.requireNonNull(ds.child("username").getValue()).toString();
                            phone = Objects.requireNonNull(ds.child("phone").getValue()).toString();
                            password = Objects.requireNonNull(ds.child("password").getValue()).toString();
                            userUid = ds.getKey();
                            getNotes();
                        } catch (NullPointerException e) {
                            Log.e("Error", "User doesn't have all info", e.getCause());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "CANCELLED", Toast.LENGTH_SHORT).show();
            }
        });

        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), AddNote.class);
            intent.putExtra(EMAIL_KEY, email);
            intent.putExtra(USERNAME_KEY, username);
            intent.putExtra(PASSWORD_KEY, password);
            intent.putExtra(PHONE_KEY, phone);
            intent.putExtra(UID_KEY, userUid);
            startActivity(intent);
            }
        });
    }

    private void getNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    if(ds.getKey() != null && Objects.equals(ds.child("uid").getValue(), userUid)) {
                        Note note = new Note(Objects.requireNonNull(ds.child("uid").getValue()).toString(),
                                Objects.requireNonNull(ds.child("title").getValue()).toString(),
                                Objects.requireNonNull(ds.child("tag").getValue()).toString(),
                                Objects.requireNonNull(ds.child("text").getValue()).toString(),
                                Objects.requireNonNull(ds.child("date").getValue()).toString());
                        notes.add(note);
                    }
                }
                setUpRcv();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUpRcv() {
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.rvNotes);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new NotesAdapter(notes);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //MenuItem item = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id==R.id.action_profile) {
           Intent intent = new Intent(getApplicationContext(), Profile.class);
           intent.putExtra(EMAIL_KEY, email);
           intent.putExtra(USERNAME_KEY, username);
           intent.putExtra(PASSWORD_KEY, password);
           intent.putExtra(PHONE_KEY, phone);
            intent.putExtra(UID_KEY, userUid);
           startActivity(intent);
            return true;
        }
        if(id==R.id.action_search) {
            SearchView searchView = (SearchView)menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return true;
    }
}