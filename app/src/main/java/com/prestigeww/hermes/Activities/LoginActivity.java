package com.prestigeww.hermes.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.NewMessageNotification;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new NewMessageNotification().notify(this,"Android Class","message check",1);
        new NewMessageNotification().notify(this,"Android project","message check 2",2);

    }
}
