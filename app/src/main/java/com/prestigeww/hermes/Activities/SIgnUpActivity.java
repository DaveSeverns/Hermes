package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.HermesConstants;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import java.util.ArrayList;
import com.prestigeww.hermes.Utilities.HermesUtiltity;
import com.prestigeww.hermes.Utilities.HermesUtiltity;


public class SIgnUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    String userId;
    String uid;
    HermesUtiltity hermesUtiltity;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final boolean isUpdate= getIntent().getBooleanExtra("updateAccount",false);
        final boolean updateProfile= getIntent().getBooleanExtra("updateProfile",false);
        hermesUtiltity = new HermesUtiltity(this);
        hermesUtiltity = new HermesUtiltity(this);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailSignUpEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordSignUpEditText);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(HermesConstants.TEST_USER_TABLE);
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
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String username = nameEditText.getText().toString().trim();
                if(new FirebaseProxy(SIgnUpActivity.this).isInternetAvailable(SIgnUpActivity.this)) {
                    if(checkForEmpty()) {
                        if(!isUpdate && !updateProfile) {
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userId = new FirebaseProxy(SIgnUpActivity.this).postRegisteredUserToFirebase(registeredUser);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType", "Registered").commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserID", userId).commit();
                            addNewUser(registeredUser,emailEditText.getText().toString()
                                    , passwordEditText.getText().toString());
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).insertUser(userId,nameEditText.getText().toString(),
                                    emailEditText.getText().toString());
                        }else if(updateProfile){
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userId = new FirebaseProxy(SIgnUpActivity.this).postUserUpdateToFirebase(registeredUser,uid);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);

                            updateUser(emailEditText.getText().toString()
                                    , passwordEditText.getText().toString());
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).UpdateUserData(userId,
                                    emailEditText.getText().toString(),nameEditText.getText().toString());

                        }else{
                            RegisteredUser registeredUser = new RegisteredUser(nameEditText.getText().toString(), emailEditText.getText().toString()
                                    , passwordEditText.getText().toString(), true);
                            userId = new FirebaseProxy(SIgnUpActivity.this).postUserUpdateToFirebase(registeredUser,uid);
                            Intent intent = new Intent(SIgnUpActivity.this, ChatThreadFeedActivity.class);
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType", "Registered").commit();

                            updateUser(emailEditText.getText().toString()
                                    , passwordEditText.getText().toString());
                            startActivity(intent);
                            new LocalDbHelper(SIgnUpActivity.this).insertUser(userId,nameEditText.getText().toString(),
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

    private boolean checkForEmpty(){
        return passwordEditText.getText() != null &&
                   nameEditText.getText() != null &&
                  emailEditText.getText() != null;
    }//end check for empty

    public void addNewUser(RegisteredUser registeredUser,String email,String password){
        if(hermesUtiltity.isValidEmail(email)){
                        if (hermesUtiltity.isValidPassword(password)){
                            registeredUser=new RegisteredUser(nameEditText.getText().toString(),emailEditText.getText().toString()
                                    ,passwordEditText.getText().toString(),true);

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserType","Registered").commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit().putString("UserID",userId).commit();

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
    public void updateUser(String email,String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.updateEmail(email)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("SIgnUpActivity", "User email address updated.");
                }
            }
        });
        user.updatePassword(password)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("SIgnUpActivity", "User password updated.");
                }
            }
        });
    }
}
