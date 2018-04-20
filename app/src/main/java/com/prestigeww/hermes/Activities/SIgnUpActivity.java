package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;


public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditText);

        //start ChatWindowActivity after click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisteredUser registeredUser=new RegisteredUser(nameEditText.getText().toString(),emailEditText.getText().toString()
                ,passwordEditText.getText().toString(),true);
                userid=new FirebaseProxy().postRegisteredUserToFirebase(registeredUser);
                Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putString("UserType","Registered").commit();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putString("UserID",userid).commit();
                startActivity(intent);
            }
        });

    }
}
