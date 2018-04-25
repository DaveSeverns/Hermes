package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import java.util.ArrayList;
import com.prestigeww.hermes.Utilities.HermesUtiltity;
import com.prestigeww.hermes.Utilities.HermesUtiltity;


public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    String userid;
    String uid;
    HermesUtiltity hermesUtiltity;
    FirebaseAuth mAuth;
    HermesUtiltity hermesUtiltity;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final boolean isUpdate= getIntent().getBooleanExtra("updateAccount",false);
        final boolean updateProfile= getIntent().getBooleanExtra("updateProfile",false);
        hermesUtiltity = new HermesUtiltity(this);
        hermesUtiltity = new HermesUtiltity(this);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditText);
        mAuth = FirebaseAuth.getInstance();
        uid= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID", null);
        if(updateProfile==true){
            ArrayList<RegisteredUser> arr=new LocalDbHelper(this).getAllUser(uid);
            nameEditText.setText(arr.get(0).username);
            emailEditText.setText(arr.get(0).email);
        }
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
                            RegisteredUser registeredUser=new RegisteredUser(nameEditText.getText().toString(),emailEditText.getText().toString()
                                    ,passwordEditText.getText().toString(),true);
                            userid=new FirebaseProxy(SIgnUpActivity.this).postRegisteredUserToFirebase(registeredUser);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType","Registered").commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserID",userid).commit();
                            startActivity(intent);
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


                if(new FirebaseProxy(SIgnUpActivity.this).isInternetAvailable(SIgnUpActivity.this)) {
                    if(checkForEmpty()) {
                        if(!isUpdate && !updateProfile) {
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userid = new FirebaseProxy(SIgnUpActivity.this).postRegisteredUserToFirebase(registeredUser);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType", "Registered").commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserID", userid).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putBoolean("SignedIn", true).commit();
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).insertUser(userid,nameEditText.getText().toString(),
                                    emailEditText.getText().toString());
                        }else if(updateProfile){
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userid = new FirebaseProxy(SIgnUpActivity.this).postUserUpdateToFirebase(registeredUser,uid);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putBoolean("SignedIn", true).commit();
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).UpdateUserData(userid,
                                    emailEditText.getText().toString(),nameEditText.getText().toString());

                        }else{
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userid = new FirebaseProxy(SIgnUpActivity.this).postUserUpdateToFirebase(registeredUser,uid);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType", "Registered").commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putBoolean("SignedIn", true).commit();
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).insertUser(userid,nameEditText.getText().toString(),
                                    emailEditText.getText().toString());
                        }
                    }else{
                        Toast.makeText(SIgnUpActivity.this,"Please Enter All the Details",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SIgnUpActivity.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkForEmpty() {
        if(passwordEditText.getText().equals(null) || nameEditText.getText().equals(null)|| emailEditText.getText().equals(null)){
            return false;
        }else{
            return true;
        }
    }

}
