package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CobaRecyclerView extends AppCompatActivity {
    DatabaseReference ref;
    ImageView menu, profil;
    TextView tvusername;
    private String usernm, email;
    private FirebaseRecyclerOptions<modelDokter> options;
    private FirebaseRecyclerAdapter<modelDokter, MyViewHolder> adapter;
    private RecyclerView recyclerView;
    private FirebaseUser users;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_dokter);
        ref = FirebaseDatabase.getInstance().getReference().child("dokter");
        tvusername = findViewById(R.id.greet);
        menu = findViewById(R.id.menu);
        profil = findViewById(R.id.prof);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        showUserProfile(currentUser);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(CobaRecyclerView.this, rvQueue.class);
                startActivity(Intent);
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CobaRecyclerView.this, profile.class);
                startActivity(intent2);
                finish();
            }
        });

        recyclerView = findViewById(R.id.listdoc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<modelDokter>().setQuery(ref, modelDokter.class).build();
        adapter = new FirebaseRecyclerAdapter<modelDokter, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull modelDokter modelDokter){
                String key = getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), detail_dokter.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                });
                holder.tvnama.setText("Drh. " + modelDokter.getNama());
                holder.tvtahun.setText(modelDokter.getTahun());
                holder.tvbidang.setText(modelDokter.getBidang());
                holder.tvharga.setText(modelDokter.getHarga());
                Picasso.get()
                        .load(modelDokter.getUrl()) // Gunakan URL dari modelDokter
                        .fit().centerCrop()
                        .placeholder(R.drawable.user) // Ganti dengan ID placeholder yang sesuai
                        .error(R.drawable.user) // Ganti dengan ID error image yang sesuai
                        .into(holder.img);
            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void showUserProfile(FirebaseUser currentUser) {
        String userID = currentUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    usernm = readUserDetails.nama;
                    tvusername.setText("Halo, " + usernm + "!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CobaRecyclerView.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
