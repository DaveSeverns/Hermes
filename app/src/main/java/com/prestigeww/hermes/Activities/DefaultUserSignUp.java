package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.hermes_logo_actionbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
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

                     mAuth.createUserWithEmailAndPassword(""+System.currentTimeMillis()+"@hermes.com","Pass@123").addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@android.support.annotation.NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userName = defaultuser.getText().toString();
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName).build();
                                        if(user != null){
                                            user.updateProfile(profileChangeRequest);
                                        }

                                        DefaultUser d=new DefaultUser();
                                        Intent intent = new Intent(DefaultUserSignUp.this, LoginActivity.class);
                                        if (defaultuser.getText().toString().equals("")) {
                                            userid = new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase();
                                        } else {
                                            d=new DefaultUser(false, defaultuser.getText().toString());
                                            userid = new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase(d);
                                        }
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                                .edit().putString("UserType", "Default").commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                                .edit().putString("UserID", userid).commit();
                                        new LocalDbHelper(DefaultUserSignUp.this).insertUser(userid,d.username,"Default@hermes.com" );
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



                }else{
                    Toast.makeText(DefaultUserSignUp.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

}
