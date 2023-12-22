package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicLong;


public class InsertAntrian extends AppCompatActivity {

    private EditText editNamaHewan, editJenisHewan, editNamaPemilik, editKontakPemilik, editGejala;
    private Button buttonantri, buttonlihat;
    private DatabaseReference queueReference;
    private String namaDokter;
    ImageView back;
    TextView NamaDokter;

    private final AtomicLong currentQueueCount = new AtomicLong(0);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_antrian);

        editNamaHewan = findViewById(R.id.editNamaHewan);
        editJenisHewan = findViewById(R.id.editJenisHewan);
        editNamaPemilik = findViewById(R.id.editNamaPemilik);
        editKontakPemilik = findViewById(R.id.editKontakPemilik);
        editGejala = findViewById(R.id.editGejala);
        buttonantri = findViewById(R.id.buttonantri);

        Intent intent = getIntent();
        if (intent != null) {
            namaDokter = intent.getStringExtra("namaDokter");
            NamaDokter = findViewById(R.id.namadokter);
            NamaDokter.setText(namaDokter);
        }

        buttonlihat = findViewById(R.id.buttonlihat);
        buttonlihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lihatIntent = new Intent(InsertAntrian.this, rvQueue.class);
                lihatIntent.putExtra("key", String.valueOf(currentQueueCount));
                lihatIntent.putExtra("namaDokter", namaDokter);
                startActivity(lihatIntent);
            }
        });

        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        queueReference = database.getReference().child("main_activity").child("antrian");
        queueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentQueueCount.set(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        buttonantri.setOnClickListener(v -> {
            String animalName = editNamaHewan.getText().toString().trim();
            String animalType = editJenisHewan.getText().toString().trim();
            String ownerName = editNamaPemilik.getText().toString().trim();
            String ownerContact = editKontakPemilik.getText().toString().trim();
            String symptomDescription = editGejala.getText().toString().trim();

            if (!TextUtils.isEmpty(animalName)
                    && !TextUtils.isEmpty(animalType)
                    && !TextUtils.isEmpty(ownerName)
                    && !TextUtils.isEmpty(ownerContact)
                    && !TextUtils.isEmpty(symptomDescription)) {

                long currentCount = currentQueueCount.getAndIncrement();
                String queueNumber = String.valueOf(currentCount);
                QueueItem queueItem = new QueueItem(animalName, animalType, ownerName, ownerContact, symptomDescription, namaDokter);


                queueReference.child(queueNumber).setValue(queueItem)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(InsertAntrian.this, "Antrian berhasil didaftarkan. Nomor antrian: " + queueNumber, Toast.LENGTH_SHORT).show();
                            currentQueueCount.getAndIncrement(); // Update jumlah antrian saat ini
                        })
                        .addOnFailureListener(e -> Toast.makeText(InsertAntrian.this, "Gagal menambahkan antrian.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(InsertAntrian.this, "Mohon lengkapi semua kolom.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
