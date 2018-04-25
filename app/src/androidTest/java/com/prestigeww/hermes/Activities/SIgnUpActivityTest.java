package com.prestigeww.hermes.Activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import com.prestigeww.hermes.Utilities.HermesUtiltity;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class SIgnUpActivityTest extends TestCase {

    private HermesUtiltity mHermesUtil;
    private Context mContext;

    @Before
    public void setup() {
        mContext =  InstrumentationRegistry.getContext();
        mHermesUtil = new HermesUtiltity(mContext.getApplicationContext());
    }

    @Test
    public void shouldReturnValidEmailResult() {
        String testEmail = "malikVK@gmail.com";
        assertTrue(mHermesUtil.isValidEmail(testEmail));
    }

    @Test
    public void shouldReturnValidPasswordResults(){
        String testPass = "Wumboqwerty1!";
        assertTrue(mHermesUtil.isValidPassword(testPass));
    }

    @Test
    public void ShouldFormatEmailToLowerCase(){
        String email = "MALIKVK@GMAIL.COM";
        String shouldBe = "malikvk@gmail.com";
        assertEquals(shouldBe, mHermesUtil.formatEmail(email));
    }

    @Test
    public void onCreate() {

    }
}