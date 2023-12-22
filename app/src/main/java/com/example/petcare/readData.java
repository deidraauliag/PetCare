package com.example.petcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class readData extends AppCompatActivity {

    TextView tvqueue, tvdocname, tvanimname, tvanimtype;
    DatabaseReference reference;
    ImageView back;
    ImageView home, profil;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);

        tvqueue = findViewById(R.id.queueNum);
        tvdocname = findViewById(R.id.doctorName);
        tvanimname = findViewById(R.id.animalName);
        tvanimtype = findViewById(R.id.animalType);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        home = findViewById(R.id.home);
        profil = findViewById(R.id.prof);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(readData.this, CobaRecyclerView.class);
                startActivity(contactIntent);
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(readData.this, profile.class);
                startActivity(contactIntent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("main_activity").child("antrian");

        String key = getIntent().getStringExtra("key");
        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Queuenum = key;
                    String doctorname = snapshot.child("namaDokter").getValue().toString();
                    String animalname = snapshot.child("animalName").getValue().toString();
                    String animaltype = snapshot.child("animalType").getValue().toString();

                    if (doctorname != null && animalname != null && animaltype != null) {
                        tvqueue.setText(Queuenum);
                        tvdocname.setText(doctorname);
                        tvanimname.setText(animalname);
                        tvanimtype.setText(animaltype);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
