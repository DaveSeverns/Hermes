package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.prestigeww.hermes.Utilities.LocalDbHelper;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class ChatThreadFeedActivityTest {
    private LocalDbHelper mLocalDbHelp;
    private Context mContext;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getContext();
        mLocalDbHelp = new LocalDbHelper(mContext.getApplicationContext());
    }



}//end
