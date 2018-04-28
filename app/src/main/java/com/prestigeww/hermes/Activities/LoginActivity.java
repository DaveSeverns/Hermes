package com.prestigeww.hermes.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.MessageInChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.Model.User;
import com.prestigeww.hermes.R;
import com.prestigeww.hermes.Utilities.FirebaseProxy;
import com.prestigeww.hermes.Utilities.HermesConstants;
import com.prestigeww.hermes.Utilities.LocalDbHelper;
import com.prestigeww.hermes.Utilities.HermesUtiltity;
import com.prestigeww.hermes.Utilities.NewMessageNotification;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;
    Button createAccountButton,defaultAccount;
    EditText emailEditText;
    EditText passwordEditText;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;
    HermesUtiltity hermesUtiltity;
    ArrayList<String> cids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        FirebaseApp.initializeApp(this);
        hermesUtiltity = new HermesUtiltity(this);
        mAuth = FirebaseAuth.getInstance();

        String uid=this.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("UserID",null);

        hermesUtiltity = new HermesUtiltity(this);
        signInButton = (Button) findViewById(R.id.signInButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        emailEditText = (EditText) findViewById(R.id.loginEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.loginPasswordEditText);
        defaultAccount=(Button)findViewById(R.id.DefaultAccountButton);
        new NewMessageNotification().notify(this,"Demo Chat ","Demo Message",1);
        //new FirebaseProxy().postChatIDInUserToFirebase("hfrhrgfwjsk");

        authStateListener =  mAuth ->{
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null){
                Intent intent = new Intent(LoginActivity.this, ChatThreadFeedActivity.class);
                getUserById();
                startActivity(intent);
            }
        };

        //open chatThreadFeedActivity after button click
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new FirebaseProxy(getApplicationContext()).isInternetAvailable(LoginActivity.this)){
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                        if(hermesUtiltity.isValidPassword(password) && hermesUtiltity.isValidEmail(email)){
                        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent intent = new Intent(LoginActivity.this, ChatThreadFeedActivity.class);
                                new LocalDbHelper(LoginActivity.this).dropTables();
                                startActivity(intent);
                            }
                        });

                        }else {
                        hermesUtiltity.showToast("Enter a valid Email or Password");
                        }
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"Internet Connection Not Available",Toast.LENGTH_LONG).show();
                }

            }
        });

        //open SignUpActivity after button click
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SIgnUpActivity.class);
                startActivity(intent);
            }
        });

        defaultAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, DefaultUserSignUp.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onPause(){
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }
    public void getUserById() {
            FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        for(DataSnapshot chatIDS:
                                dataSnapshot.child("ChatID").getChildren()) {
                            String c = chatIDS.getValue().toString();
                            Log.e("Data Snap",c);
                            cids.add(c);

                        }
                        String em;
                        if(dataSnapshot.child("isRegistered").getValue().equals(true)) {
                            em = dataSnapshot.child("email").getValue().toString();
                        }else{
                            em="Default@hermes.com";
                        }
                        String uname = dataSnapshot.child("username").getValue().toString();
                        Log.e("DaTA snap um",uname);
                        Log.e("DaTA snap em",em);
                        LocalDbHelper lb=new LocalDbHelper(LoginActivity.this);
                        lb.dropTables();
                        lb.insertChatmember(cids);
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit().putString("UserName", uname).commit();
                        lb.insertUser(mAuth.getCurrentUser().getUid(),uname,em);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FB error: ", databaseError.getDetails());
                }
            });

    }
}