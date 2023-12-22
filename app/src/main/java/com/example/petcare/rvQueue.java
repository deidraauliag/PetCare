package com.example.petcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class rvQueue extends AppCompatActivity {
    DatabaseReference reference;
    ImageView home, profil;
    private FirebaseRecyclerOptions<QueueModel> options;
    private FirebaseRecyclerAdapter<QueueModel, vhQueue> adapter;
    private RecyclerView recyclerView;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        back = findViewById(R.id.btn_back);
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
                Intent contactIntent = new Intent(rvQueue.this, CobaRecyclerView.class);
                startActivity(contactIntent);
            }
        });
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(rvQueue.this, profile.class);
                startActivity(contactIntent);
            }
        });

        recyclerView = findViewById(R.id.listQueue);
        reference = FirebaseDatabase.getInstance().getReference().child("main_activity").child("antrian");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        options = new FirebaseRecyclerOptions.Builder<QueueModel>().setQuery(reference, QueueModel.class).build();
        adapter = new FirebaseRecyclerAdapter<QueueModel, vhQueue>(options){
            @Override
            protected void onBindViewHolder(@NonNull vhQueue holder, int position, @NonNull QueueModel queueModel){
                String key = getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), readData.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                });

                holder.tvnamadokter.setText(queueModel.getNamaDokter());
                holder.tvanimname.setText(queueModel.getAnimalName());
                holder.tvanimtype.setText("(" + queueModel.getAnimalType() + ")");
            }
            @NonNull
            @Override
            public vhQueue onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_queue, parent, false);
                return new vhQueue(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}