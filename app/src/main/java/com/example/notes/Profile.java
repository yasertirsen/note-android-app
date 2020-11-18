package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;

public class Profile extends AppCompatActivity {

    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvUsername;

    private String email;
    private String username;
    private String phone;
    private String password;

    public static final String USERNAME_KEY = "USERNAME_KEY";
    public static final String PHONE_KEY = "PHONE_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent homeIntent = getIntent();
        email = homeIntent.getStringExtra(EMAIL_KEY);
        username = homeIntent.getStringExtra(USERNAME_KEY);
        password = homeIntent.getStringExtra(PASSWORD_KEY);
        phone = homeIntent.getStringExtra(PHONE_KEY);

        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvUsername = (TextView)findViewById(R.id.tvUsername);

        tvEmail.setText(email);
        tvUsername.setText(username);
        tvPhone.setText(phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id==R.id.action_change_info) {
            Intent intent = new Intent(getApplicationContext(), EditInfo.class);
            intent.putExtra(EMAIL_KEY, email);
            intent.putExtra(USERNAME_KEY, username);
            intent.putExtra(PHONE_KEY, phone);
            intent.putExtra(PASSWORD_KEY, password);
            startActivity(intent);
            return true;
        }
        return true;
    }
}