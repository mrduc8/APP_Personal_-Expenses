package com.example.assignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Dangnhap extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private SharedPreferences prefs;
    TextInputLayout inputLayout;
    private FirebaseAuth mAuth;
    private EditText edUsername, edPass;
    public CheckBox cbLuuThongTin;
    private TextView qmk;
    public static final String FULLNAME = "FULLNAME";
    public static final String LEVEL = "LEVEL";
    public static final String USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dangnhap);
        getSupportActionBar().hide();
        mappingView();


        setupListener();

    }

   final private void mappingView() {
        edUsername = findViewById(R.id.edtUserName);
        edPass = findViewById(R.id.edtPassword);
        qmk = findViewById(R.id.quenmatkhau);
    }

    private void setupListener(){
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            startActivity(new Intent(Dangnhap.this, DangKiActivity.class));
        });

        qmk.setOnClickListener(v -> {
            startActivity(new Intent(Dangnhap.this, Datlaimatkhaubanggmail.class));
        });

        findViewById(R.id.btnLogin).setOnClickListener(v -> {

            String userNameInput = edUsername.getText().toString().trim();
            String passwordInput = edPass.getText().toString().trim();


            Boolean checkError = true;
            if(userNameInput.isEmpty()){
                edUsername.setError("Email không được để trống!");
                checkError = false;
            }

            if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", userNameInput)){
                edUsername.setError("Email sai định dạng!");
                checkError = false;
            }

            if(passwordInput.isEmpty()){
                edPass.setError("Mật khẩu không được để trống!");
                checkError = false;
            }
            if(passwordInput.length()<6){
                edPass.setError("Mật khẩu quá yếu phải trên 6 ký tự!");

            }
if (checkError == true) {
    mAuth.signInWithEmailAndPassword(userNameInput, passwordInput)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information
                        final ProgressDialog progressDialog = ProgressDialog.show(Dangnhap.this, "Thông Báo", "Đang đăng nhập...");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                sendNotification();
                                String name = user.getDisplayName();
                                if (name == null) {
                                    Intent introIntent = new Intent(Dangnhap.this, Capnhatthongtinnguoidung.class);
                                    startActivity(introIntent);
                                    finishAffinity();
                                } else {
                                    startActivity(new Intent(Dangnhap.this, MainActivity.class));
                                    finishAffinity();
                                }
                            }
                        }, 2000);

                    } else {
                        // If sign in fails, display a message to the user.
                        edUsername.setError("Sai tài khoản Email");
                        edPass.setError("Sai mật khẩu");

                    }
                }
            });
}
        });
    }

    private void  sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mimap);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(Dangnhap.this, NotificationApplication.CHANNEL_ID)
                .setContentTitle("Chào bạn nhé!")
                .setContentText("Hôm nay thật là tuyệt vời khi được gặp bạn! Chúc bạn có một ngày thật vui vẻ vào hạnh phúc nhé!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Hôm nay thật là tuyệt vời khi được gặp bạn! Chúc bạn có một ngày thật vui vẻ vào hạnh phúc nhé!"))
                .setSmallIcon(R.drawable.iconnho)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

}