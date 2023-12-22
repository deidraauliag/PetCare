package com.example.petcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    private TextView tvedit, tvusername, tvNoHp, tvemailP;
    private String usernm, nohp, email;
    private ImageView backProfil, home, menu, avatar, tvAva;
    private FirebaseAuth mAuth;
    private AppCompatButton logout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvedit = findViewById(R.id.edit);
        tvusername = findViewById(R.id.username);
        tvNoHp = findViewById(R.id.NoHp);
        tvemailP = findViewById(R.id.emailP);
        backProfil = findViewById(R.id.backProfil);
        logout = findViewById(R.id.logout);
        home = findViewById(R.id.home);
        menu = findViewById(R.id.menu);
        avatar = findViewById(R.id.avatarPic);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            Toast.makeText(profile.this, "Something went wrong",
                    Toast.LENGTH_LONG).show();
        } else{
            showUserProfile(firebaseUser);
        }

        backProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, editProf.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //button home navbar yang kiri
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, CobaRecyclerView.class);
                startActivity(intent);
            }
        });

        //button menu navbar yang tengah
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, rvQueue.class);
                startActivity(intent);
            }
        });

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    usernm = readUserDetails.nama;
                    email = firebaseUser.getEmail();
                    nohp = readUserDetails.nohp;
                    tvusername.setText(usernm);
                    tvemailP.setText(email);
                    tvNoHp.setText(nohp);

                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(avatar);
                } else {
                    Toast.makeText(profile.this, "Something went wrong",
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logoutUser() {
        mAuth.signOut();
        // After signing out, navigate to the login screen or any other screen
        Intent intent = new Intent(profile.this, login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}