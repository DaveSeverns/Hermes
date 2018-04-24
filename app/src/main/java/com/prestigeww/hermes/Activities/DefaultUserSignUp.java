package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prestigeww.hermes.Model.DefaultUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.LocalDbHelper;

import java.util.ArrayList;

public class DefaultUserSignUp extends AppCompatActivity {
    EditText defaultuser;
    Button submit;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user);
        defaultuser=(EditText)findViewById(R.id.dusernameEditText);
        submit=(Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new FirebaseProxy().isInternetAvailable(DefaultUserSignUp.this)) {
                    if (defaultuser.getText().toString().equals("")) {
                        userid = new FirebaseProxy().postDefaultUserToFirebase();
                    } else {
                        userid = new FirebaseProxy().postDefaultUserToFirebase(new DefaultUser(false, defaultuser.getText().toString()));
                    }
                    Intent i = new Intent(DefaultUserSignUp.this, ChatThreadFeedActivity.class);
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putString("UserType", "Default").commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putString("UserID", userid).commit();
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit().putBoolean("SignedIn", true).commit();
                    startActivity(i);

                }else{
                    Toast.makeText(DefaultUserSignUp.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
