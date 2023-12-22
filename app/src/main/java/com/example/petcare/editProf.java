package com.example.petcare;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class editProf extends AppCompatActivity {
    private AppCompatButton simpanEdit;
    private EditText editNama, editNohp;
    private String id, usernm, nohp;
    private ProgressDialog progressDialog;
    private ImageView avatar, backEdit;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    Uri path;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        editNama = findViewById(R.id.uname);
        editNohp = findViewById(R.id.nohp);
        avatar = findViewById(R.id.avatar);
        simpanEdit = findViewById(R.id.simpanEdit);
        backEdit = findViewById(R.id.backEdit);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("image");

        if(firebaseUser == null){
            Toast.makeText(editProf.this, "Something went wrong",
                    Toast.LENGTH_LONG).show();
        } else{
            showUserProfile(firebaseUser);
        }

        //button back
        backEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        avatar.setOnClickListener(v -> {
            Uri uri = firebaseUser.getPhotoUrl();
            Picasso.get().load(uri).into(avatar);
            selectImage();
        });

        progressDialog = new ProgressDialog(editProf.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan");

        simpanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
                uploadPic();
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser){
        usernm = editNama.getText().toString();
        nohp = editNohp.getText().toString();
        if (TextUtils.isEmpty(usernm)) {
            Toast.makeText(editProf.this, "Masukkan Nama", Toast.LENGTH_LONG).show();
            editNama.setError("Name is required");
            editNama.requestFocus();
        } else if (TextUtils.isEmpty(nohp)) {
            Toast.makeText(editProf.this, "Masukkan Nomor Hp", Toast.LENGTH_LONG).show();
            editNohp.setError("Phone Number is required");
            editNohp.requestFocus();
        } else {
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(usernm, nohp);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            String userID = firebaseUser.getUid();
            ref.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(usernm).build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(editProf.this, "Pembaruan berhasil", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(editProf.this, profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(editProf.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        simpanEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usrnm = editNama.getText().toString();
                String nohp = editNohp.getText().toString();
                if (editNama.getText().length()>0 && editNohp.getText().length()>0) {

                    // Call the upload method if all fields are filled
                    upload(editNama.getText().toString(), editNohp.getText().toString());

                    // Start the new activity with intent extras
                    Intent intent3 = new Intent(editProf.this, profile.class);
                    intent3.putExtra("username", usrnm);
                    intent3.putExtra("nohp", nohp);
                    startActivity(intent3);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Mohon isi semua data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void selectImage(){
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(editProf.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items,((dialog, item) -> {
            if(items[item].equals("Ambil Foto")){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
            } else if(items[item].equals("Pilih dari Galeri")){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 20);
            } else if (items[item].equals("Batal")){
                dialog.dismiss();
            }
        }));
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20 && resultCode == RESULT_OK && data != null){
            path= data.getData();
            avatar.setImageURI(path);
//            Thread thread = new Thread(() -> {
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(path);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    avatar.post(() -> {
//                        avatar.setImageBitmap(bitmap);
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            thread.start();
        }

//        if(requestCode == 10 && resultCode == RESULT_OK){
//            final Bundle extras = data.getExtras();
//            Thread thread = new Thread(() -> {
//                Bitmap bitmap = (Bitmap) extras.get("data");
//                avatar.post(() -> {
//                    avatar.setImageBitmap(bitmap);
//                });
//            });
//            thread.start();
//        }
    }

    private void upload(String uname, String nohp){
        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //upload
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("image").child("IMG"+new Date().getTime()+".jpeg");
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata()!= null){
                    if(taskSnapshot.getMetadata().getReference()!=null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult() != null){
                                    saveData(uname, nohp, task.getResult().toString());
//                                    upload(editNama.getText().toString(), editNohp.getText().toString());
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData(String uname, String nohp, String avatar){
        Map<String, Object> user = new HashMap<>();
        user.put("uname", uname);
        user.put("nohp", nohp);
        user.put("avatar", avatar);

        progressDialog.show();
        db.getReference()
                .child("user")
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "Sukses", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
                    nohp = readUserDetails.nohp;
                    editNama.setText(usernm);
                    editNohp.setText(nohp);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(editProf.this, "Something went wrong",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.commonn_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.menu_refresh){
//            startActivity(getIntent());
//            finish();
//            overridePendingTransition(0,0);
//        } else if (id == R.id.menu_logout) {
//            mAuth.signOut();
//            Toast.makeText(editProf3.this, "Logged Out", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(editProf3.this, login.class);
//        }
//    }

    private void uploadPic(){
        if(path != null){
            StorageReference file = storageReference.child(mAuth.getCurrentUser().getUid() + "."
                    + getFileExtension(path));
            file.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            firebaseUser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates =  new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUrl).build();
                            firebaseUser.updateProfile(profileUpdates);

                            Intent intent = new Intent(editProf.this, profile.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}


