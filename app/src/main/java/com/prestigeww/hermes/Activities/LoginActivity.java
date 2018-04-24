package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.NewMessageNotification;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;
    Button createAccountButton,defaultAccount;
    EditText emailEditText;
    EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        boolean temp=this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("SignedIn",false);
        String uid=this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);
        if(temp==true){
            Intent intent = new Intent(LoginActivity.this, ChatThreadFeedActivity.class);
            startActivity(intent);
        }
        signInButton = (Button) findViewById(R.id.signInButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        emailEditText = (EditText) findViewById(R.id.loginEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.loginPasswordEditText);
        defaultAccount=(Button)findViewById(R.id.DefaultAccountButton);
        new NewMessageNotification().notify(this,"Demo Chat ","Demo Message",1);
        //open chatThreadFeedActivity after button click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new FirebaseProxy().isInternetAvailable(LoginActivity.this)){
                    Intent intent = new Intent(LoginActivity.this, ChatThreadFeedActivity.class);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putBoolean("SignedIn", true).commit();
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });

        //open SignUpActivity after button click
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SIgnUpActivity.class);
                startActivity(intent);
            }
        });

        defaultAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DefaultUserSignUp.class);
                startActivity(intent);
            }
        });

    }
}