package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prestigeww.hermes.R;


public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;

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
                Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                startActivity(intent);
            }
        });


    }
}
