package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.notes.MainActivity.EMAIL_KEY;
import static com.example.notes.MainActivity.PASSWORD_KEY;
import static com.example.notes.NotesAdapter.MyViewHolder.UID_KEY;

public class Profile extends AppCompatActivity {

    private static final String TAG = "";
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvUsername;
    private CircleImageView profileImageView;

    private String email;
    private String username;
    private String phone;
    private String password;
    private String uid;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    int TAKE_IMAGE_CODE = 10001;

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
        uid = homeIntent.getStringExtra(UID_KEY);

        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        profileImageView = (CircleImageView)findViewById(R.id.profileImageView);

        tvEmail.setText(email);
        tvUsername.setText(username);
        tvPhone.setText(phone);

        if(user != null) {
            if(user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImageView);
            }
        }
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
            intent.putExtra(UID_KEY, uid);
            startActivity(intent);
            return true;
        }
        return true;
    }

    public void handleImageClick(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intent, TAKE_IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ",e.getCause() );
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Profile image failed...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}