package com.prestigeww.hermes;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Utilities.FirebaseProxy;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirebaseProxyUnitTest extends TestCase {
    private FirebaseProxy proxy;
    private ChatThread testThread;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        proxy = new FirebaseProxy();
        testThread = new ChatThread();
    }

    @Test
    public void testShouldReturnNotNullOnAddChatThread(){

        assertNotNull(proxy.addNewThreadToFirebase(testThread));
    }
}
