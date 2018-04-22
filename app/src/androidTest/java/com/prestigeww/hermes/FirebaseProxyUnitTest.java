package com.prestigeww.hermes;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Utilities.FirebaseProxy;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class FirebaseProxyUnitTest extends TestCase {
    private FirebaseProxy proxy;
    private ChatThread testThread;
    ArrayList<String> chatIdsTest;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        chatIdsTest = new ArrayList<String>();
        chatIdsTest.add("-LAiA-q-_6YBbTiGTwaS");
        proxy = new FirebaseProxy();
        testThread = new ChatThread();
    }

    @Test
    public void testShouldReturnNotNullOnAddChatThread(){

        assertNotNull(proxy.postThreadToFirebase(testThread));
    }



    @Test
    public void testReadChatThreadShouldReturnNumberOfIds(){
        ArrayList<ChatThread> threadsForThisTest = new ArrayList<>(proxy.getChatsById(chatIdsTest));
        assertEquals(1,threadsForThisTest.size());
    }
}
