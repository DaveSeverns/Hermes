package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.HermesUtiltity;


public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    String userid;
    HermesUtiltity hermesUtiltity;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        hermesUtiltity = new HermesUtiltity(this);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditText);
        mAuth = FirebaseAuth.getInstance();
        //start ChatWindowActivity after click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = nameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
                    if(hermesUtiltity.isValidEmail(email)){
                        if (hermesUtiltity.isValidPassword(password)){
                            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SIgnUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        task.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }

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
