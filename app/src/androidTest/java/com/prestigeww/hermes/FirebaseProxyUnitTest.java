package com.prestigeww.hermes;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
    private Context context;
    private Context mockContext;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockContext = InstrumentationRegistry.getContext();
        chatIdsTest = new ArrayList<String>();
        chatIdsTest.add("-LAiA-q-_6YBbTiGTwaS");
        proxy = new FirebaseProxy(mockContext);
        chatIdsTest.add("-LAiA-q-_6YBbTiGTwaS");
        context= InstrumentationRegistry.getContext();
        proxy = new FirebaseProxy(context);
        testThread = new ChatThread();
    }

    @Test
    public void testShouldReturnNotNullOnAddChatThread(){

        assertNotNull(proxy.postThreadToFirebase(testThread));
    }



   //@Test
   //public void testReadChatThreadShouldReturnNumberOfIds(){
   //    ArrayList<ChatThread> threadsForThisTest = new ArrayList<>(proxy.getChatsById(chatIdsTest,Instrumentati));
   //    assertEquals(1,threadsForThisTest.size());
   //}
}
