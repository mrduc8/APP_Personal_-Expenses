package com.example.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Capnhatthongtinnguoidung extends AppCompatActivity {

    private EditText tilUsername, tilHoTen, tilMail, tilBirthday, tilPhone;
    private static final int REQUEST_IMAGE_OPEN = 2;
    private ImageView imgImageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhatthongtinnguoidung);
        final StorageReference storageRef = storage.getReference();

        getSupportActionBar().setTitle("THÔNG TIN NGƯỜI DÙNG");
        mappingView();

        imgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });

        showAllUserData();

        findViewById(R.id.btn_ok_capNhatActivity).setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Calendar calendar = Calendar.getInstance();
            String nameUser = tilHoTen.getText().toString().trim();
            StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis() +".png");
            Boolean checkError = true;
            if(tilHoTen.getText().toString().trim().isEmpty()){
                tilHoTen.setError("Tên không được để trống");
                checkError = false;
            }

            if(checkError){
                final ProgressDialog progressDialoga = ProgressDialog.show(Capnhatthongtinnguoidung.this,"Thông Báo","Đang cập nhật dữ liệu 0%...");
                // Get the data from an ImageView as bytes
                imgImageView.setDrawingCacheEnabled(true);
                imgImageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgImageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                progressDialoga.dismiss();
                final ProgressDialog progressDialogc = ProgressDialog.show(Capnhatthongtinnguoidung.this,"Thông Báo","Đang cập nhật dữ liệu 20%...");
                uploadTask.addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception exception) {


                        // Handle unsuccessful uploads
                        Toast.makeText(Capnhatthongtinnguoidung.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialogc.dismiss();
                        final ProgressDialog progressDialogb = ProgressDialog.show(Capnhatthongtinnguoidung.this,"Thông Báo","Đang cập nhật dữ liệu 70%...");
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameUser)
                                        .setPhotoUri(Uri.parse(String.valueOf(uri)))
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialogb.dismiss();
                                                    final ProgressDialog progressDialog = ProgressDialog.show(Capnhatthongtinnguoidung.this,"Thông Báo","Đang cập nhật dữ liệu 100%...");
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressDialog.dismiss();
                                                            startActivity(new Intent(Capnhatthongtinnguoidung.this, MainActivity.class));
                                                            finishAffinity();
                                                        }
                                                    },1000);
                                                }
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri full = data.getData();
            ImageView imgv = findViewById(R.id.imgavatar);
            imgv.setImageURI(full);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAllUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        tilHoTen.setText(name);
        tilMail.setText(email);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(imgImageView);
        }

    }

    private void mappingView() {
        imgImageView = (ImageView) findViewById(R.id.imgavatar);
        tilHoTen = (EditText)  findViewById(R.id.til_hoTen_capNhatActivity);
        tilMail = findViewById(R.id.til_mail_capNhatActivity);
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(Capnhatthongtinnguoidung.this, MainActivity.class));
            finishAffinity();
    }
}