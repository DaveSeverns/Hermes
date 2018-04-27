package com.prestigeww.hermes;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.Display;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.prestigeww.hermes.Model.ChatThread;
import com.prestigeww.hermes.Model.DefaultUser;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.Utilities.FirebaseProxy;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class FirebaseProxyUnitTest extends TestCase {

    private FirebaseProxy proxy;
    private ChatThread testThread,
            testThread2;
    ArrayList<String> chatIdsTest;
    private Context mockContext;
    private Context context2;
    public FirebaseApp firebaseApp;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mDatabaseReference;
    public static int size;
    private DefaultUser defaultUser;
    private RegisteredUser registeredUser;
    public StorageReference mHermesStorage;
    private FirebaseProxy.FirebaseProxyInterface firebaseProxyInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockContext = InstrumentationRegistry.getContext();

        chatIdsTest = new ArrayList<String>();
        chatIdsTest.add("-LAiA-q-_6YBbTiGTwaS");
        proxy = new FirebaseProxy(mockContext);
        testThread = new ChatThread();
        testThread2 = new ChatThread();
        defaultUser = new DefaultUser(true, "bobby");
        registeredUser = new RegisteredUser("malik", "qwerty@tumail.com","Password#1", true);
        firebaseProxyInterface = new FirebaseProxy.FirebaseProxyInterface() {
            @Override
            public void getChatThread(ChatThread thread) {

            }
        };
    }

    @Test
    public void testShouldReturnNotNullOnAddChatThread(){

        assertNotNull(proxy.postThreadToFirebase(testThread));
    }

    /*    @Test
        public void testReadChatThreadShouldReturnNumberOfIds(){
            ArrayList<ChatThread> threadsForThisTest = new ArrayList<>(proxy.getChatsById(chatIdsTest, firebaseProxyInterface));
            assertNotNull(threadsForThisTest);
            assert threadsForThisTest.add(testThread);
            assert threadsForThisTest.add(testThread2);
            assertEquals(0,threadsForThisTest.size());
            //assertEquals(1,threadsForThisTest.size());
        }
//    */
    @Test
    public void ShouldPossibly_postToFireBase(){
        assertNotNull(proxy.postThreadToFirebase(testThread));
    }//

    @Test
    public void ShouldPossibly_postDefaultUserToFirebase(){
        assertNotNull(proxy.postDefaultUserToFirebase());
    }//

    @Test
    public void ShouldPossibly_postDefaultUserToFirebase2(){
        assertNotNull(proxy.postDefaultUserToFirebase(defaultUser));
    }//

    @Test
    public void ShouldPossibly_postRegisteredUserToFirebase(){
        assertNotNull(proxy.postRegisteredUserToFirebase(registeredUser));
    }//

    @Test
    public void ShouldPossibly_postChatIDInUserToFirebase(){
        proxy.postChatIDInUserToFirebase(chatIdsTest, "123456");
    }//

}//end
