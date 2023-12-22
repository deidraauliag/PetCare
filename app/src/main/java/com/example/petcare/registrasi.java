package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registrasi extends AppCompatActivity {
    private EditText etNama, etEmail, etPass;
    private Button btnMasuk, btnDaftar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi);
        Toast.makeText(registrasi.this, "You can Register now", Toast.LENGTH_LONG).show();
        etNama = (EditText) findViewById(R.id.nama);
        etEmail = (EditText) findViewById(R.id.email);
        etPass = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        btnMasuk = (Button) findViewById(R.id.btn_login);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.btn_login) {
                    Intent loginIntent = new Intent(registrasi.this, login.class);
                    startActivity(loginIntent);
                }
            }
        });

        btnDaftar = findViewById(R.id.btn_register);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textName = etNama.getText().toString();
                String textEmail = etEmail.getText().toString();
                String textPassword = etPass.getText().toString();
                if(TextUtils.isEmpty(textName)){
                    Toast.makeText(registrasi.this, "Masukkan Nama", Toast.LENGTH_LONG).show();
                    etNama.setError("Name is required");
                    etNama.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(registrasi.this, "Masukkan Email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(registrasi.this, "Masukkan Password", Toast.LENGTH_LONG).show();
                    etPass.setError("Password is required");
                    etPass.requestFocus();
                } else if(textPassword.length()<6){
                    Toast.makeText(registrasi.this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show();
                    etPass.setError("Password too weak");
                    etPass.requestFocus();
                } else {
                    registerUser(textName, textEmail, textPassword);
                }
            }
        });
    }

    private void registerUser(String textName, String textEmail, String textPassword){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(registrasi.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registrasi.this, "Berhasil", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textName);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

                            ref.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(registrasi.this, "Berhasil", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(registrasi.this, CobaRecyclerView.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(registrasi.this, "Berhasil", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e){
                                etPass.setError("Akun telah Terdaftra");
                                etPass.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(registrasi.this, "RegisterActivity", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}