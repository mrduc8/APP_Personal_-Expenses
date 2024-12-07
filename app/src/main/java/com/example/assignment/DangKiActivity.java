package com.example.assignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class DangKiActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    Button btnDangKi, btnHuyBo;
    private FirebaseAuth mAuth;
    EditText etPassword, etRePassword, etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangky);
        getSupportActionBar().hide();

        mappingView();

        dangKi();
        btnHuyBo.setOnClickListener(v -> {
            startActivity(new Intent(DangKiActivity.this, Dangnhap.class));
        });
    }

    private void dangKi() {
        btnDangKi.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();

            String pass = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String rePass = etRePassword.getText().toString().trim();


            Boolean checkError = true;

            if(rePass.isEmpty()){
                etRePassword.setError("Nhập lại mật khẩu không được bỏ trống!");
                checkError = false;
            }
            if(pass.length()<6){
                etPassword.setError("Mật khẩu quá yếu cần đủ 6 ký tự!");
                checkError = false;
            }
            if(!rePass.equals(pass)){
                etRePassword.setError("Nhập lại mật khẩu không đúng!");
                checkError = false;
            }
            if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", email)){
                etEmail.setError("Email sai định dạng!");
                checkError = false;
            }

            if(checkError){
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    // Sign in success, update UI with the signed-in user's information
                                    final ProgressDialog progressDialog = ProgressDialog.show(DangKiActivity.this,"Thông Báo","Đang đăng ký...");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            sendNotification();
                                            String name = user.getDisplayName();
                                            if (name == null) {
                                                Toast.makeText(DangKiActivity.this, "Đăng ký thành công!",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent introIntent = new Intent(DangKiActivity.this, MainActivity.class);
                                                startActivity(introIntent);
                                                finishAffinity();
                                            } else {
                                                startActivity(new Intent(DangKiActivity.this, MainActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    },2000);

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(DangKiActivity.this, "Email này đã tồn tại!",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }

    private void  sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mimap);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(DangKiActivity.this, NotificationApplication.CHANNEL_ID)
                .setContentTitle("Đăng ký thành công!")
                .setContentText("Chào mừng bạn mới, chúc bạn có một ngày vui vẻ!")
                .setSmallIcon(R.drawable.iconnho)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

    private void mappingView() {
        btnDangKi = findViewById(R.id.btnReg);
        btnHuyBo = findViewById(R.id.btnRelay);
        etEmail = findViewById(R.id.dangKi_etEmail);
        etPassword = findViewById(R.id.edtRegPassword);
        etRePassword = findViewById(R.id.edtRePassword);
    }
}
