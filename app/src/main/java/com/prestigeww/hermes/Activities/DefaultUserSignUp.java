package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.prestigeww.hermes.Model.DefaultUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.HermesUtiltity;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DefaultUserSignUp extends AppCompatActivity {
    EditText defaultuser;
    Button submit;
    String userid;
    HermesUtiltity hermesUtiltity;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        FirebaseApp.initializeApp(getApplicationContext());

        defaultuser=(EditText)findViewById(R.id.dusernameEditText);
        submit=(Button)findViewById(R.id.submitButton);
         mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (new FirebaseProxy(DefaultUserSignUp.this).isInternetAvailable(DefaultUserSignUp.this)) {
                    if (defaultuser.getText().toString().equals("")) {
                        userid = new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase();
                    } else {
                        userid = new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase(new DefaultUser(false, defaultuser.getText().toString()));
                    }
                    Intent in = new Intent(DefaultUserSignUp.this, LoginActivity.class);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putString("UserType", "Default").commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putString("UserID", userid).commit();
                     mAuth.createUserWithEmailAndPassword(userid+"@hermes.com","Pass@123").addOnCompleteListener( new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@android.support.annotation.NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(DefaultUserSignUp.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        task.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@android.support.annotation.NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                }
                            });
                    startActivity(in);

                }else{
                    Toast.makeText(DefaultUserSignUp.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
