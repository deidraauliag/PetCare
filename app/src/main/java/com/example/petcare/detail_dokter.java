package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class detail_dokter extends AppCompatActivity{
    TextView tvnama, tvtahun, tvbidang, tvalumnus, tvstp, tvharga, url;
    ImageView back;
    ImageView img;
    Button btnpilih;
    DatabaseReference ref;
    DatabaseReference imgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_dokter);
        tvnama = findViewById(R.id.nama);
        tvtahun = findViewById(R.id.tahun);
        tvbidang = findViewById(R.id.bidang);
        tvalumnus = findViewById(R.id.alumnus);
        tvstp = findViewById(R.id.stp);
        tvharga = findViewById(R.id.hargadoc);

        url = findViewById(R.id.url);
        img = findViewById(R.id.imgFoto);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnpilih = findViewById(R.id.btnpilih);
        btnpilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(detail_dokter.this, InsertAntrian.class);
                contactIntent.putExtra("namaDokter", tvnama.getText().toString());
                startActivity(contactIntent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("dokter");
//        imgRef = ref.child("url");


        String key = getIntent().getStringExtra("key");
        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nama = dataSnapshot.child("nama").getValue().toString();
                String tahun = dataSnapshot.child("tahun").getValue().toString();
                String bidang = dataSnapshot.child("bidang").getValue().toString();
                String alumnus = dataSnapshot.child("alumnus").getValue().toString();
                String stp = dataSnapshot.child("stp").getValue().toString();
                String harga = dataSnapshot.child("harga").getValue().toString();
                String message = dataSnapshot.child("url").getValue().toString();

                tvnama.setText("Drh. " + nama);
                tvtahun.setText(tahun);
                tvbidang.setText(bidang);
                tvalumnus.setText(alumnus);
                tvstp.setText(stp);
                tvharga.setText(harga);

                url.setText(message);
                Picasso.get()
                        .load(message)
                        .into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}