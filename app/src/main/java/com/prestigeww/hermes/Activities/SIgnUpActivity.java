package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import java.util.ArrayList;

public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    String userid;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final boolean isUpdate= getIntent().getBooleanExtra("updateAccount",false);
        final boolean updateProfile= getIntent().getBooleanExtra("updateProfile",false);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditText);
        uid= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID", null);
        if(updateProfile==true){
            ArrayList<RegisteredUser> arr=new LocalDbHelper(this).getAllUser(uid);
            nameEditText.setText(arr.get(0).username);
            emailEditText.setText(arr.get(0).email);
        }
        //start ChatWindowActivity after click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new FirebaseProxy().isInternetAvailable(SIgnUpActivity.this)) {
                    if(checkForEmpty()) {
                        if(!isUpdate && !updateProfile) {
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userid = new FirebaseProxy().postRegisteredUserToFirebase(registeredUser);
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
                            userid = new FirebaseProxy().postUserUpdateToFirebase(registeredUser,uid);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putBoolean("SignedIn", true).commit();
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).UpdateUserData(userid,
                                    emailEditText.getText().toString(),nameEditText.getText().toString());

                        }else{
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userid = new FirebaseProxy().postUserUpdateToFirebase(registeredUser,uid);
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

    public boolean checkForEmpty(){
        if(emailEditText.getText().toString().equals("") || nameEditText.getText().toString().equals("")
                ||passwordEditText.getText().toString().equals("")){
            return false;
        }else {
            return true;
        }
    }

}