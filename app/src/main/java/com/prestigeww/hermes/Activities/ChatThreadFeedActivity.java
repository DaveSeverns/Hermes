package com.prestigeww.hermes.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.prestigeww.hermes.R;


public class ChatThreadFeedActivity extends AppCompatActivity {

    private String mansNotHot;
    private String BigShaq = "Mans Never Hot";

    //Testing Krati

    public String getBigShaq() {
        return BigShaq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_thread_feed);
    }
}
