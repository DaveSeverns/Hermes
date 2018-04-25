package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.prestigeww.hermes.Model.DefaultUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;

public class DefaultUserSignUp extends AppCompatActivity {
    EditText defaultuser;
    Button submit;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_user);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        defaultuser=(EditText)findViewById(R.id.dusernameEditText);
        submit=(Button)findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defaultuser.getText().toString().equals(null)){
                    userid=new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase();
                }
                else {
                    userid=new FirebaseProxy(DefaultUserSignUp.this).postDefaultUserToFirebase(new DefaultUser(false,defaultuser.getText().toString()));
                }
                Intent i= new Intent(DefaultUserSignUp.this, ChatThreadFeedActivity.class);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putString("UserType","Default").commit();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit().putString("UserID",userid).commit();
                startActivity(i);
            }
        });
    }
}
