package com.HafizyahRayhanZulikhramJBusBR.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

public class LoginActivity extends AppCompatActivity {
    private TextView registerNow=null;
    private Button loginButton =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerNow = findViewById(R.id.register_now);
        loginButton = findViewById(R.id.login_button);
        registerNow.setOnClickListener( view -> {moveActivity(this, RegisterActivity.class);});
        loginButton.setOnClickListener( view -> {moveActivity(this, MainActivity.class);});
        getSupportActionBar().hide();
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent (ctx,cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
}