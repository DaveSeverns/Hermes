package com.prestigeww.hermes.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.prestigeww.hermes.Model.MessageInChat;
import com.prestigeww.hermes.Model.RegisteredUser;
import com.prestigeww.hermes.Model.User;

public class LocalDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Hermes.db";
    public static final String MESSAGE_TABLE_NAME = "Messages";
    public static final String MESSAGE_COLUMN_ID = "MessID";
    public static final String MESSAGE_COLUMN_BODY = "Body";
    public static final String MESSAGE_COLUMN_SENDERID = "SenderID";
    public static final String MESSAGE_COLUMN_DOCURI = "DocURI";
    public static final String MESSAGE_COLUMN_CHATID = "ChatID";
    public static final String MESSAGE_COLUMN_TIME = "Time";
    public static final String USER_TABLE_NAME = "User";
    public static final String USER_COLUMN_ID = "UserID";
    public static final String USER_COLUMN_USERNAME = "UserName";
    public static final String USER_COLUMN_EMAIL = "Email";
    public static final String CHATMEMBER_TABLE_NAME = "Chatmember";
    public static final String CHATMEMBER_COLUMN_CHATID="ChatID";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ MESSAGE_TABLE_NAME +
                        "(MessID txt primary key, Body text,SenderID text,DocURI text, ChatID text,Time text)"
        );
        db.execSQL(
                "create table "+ USER_TABLE_NAME +
                        "(UserID txt primary key, UserName text,Email text)"
        );
        db.execSQL(
                "create table "+ CHATMEMBER_TABLE_NAME +
                        "(ChatID text primary key)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+CHATMEMBER_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMessage (String mid, String body, String senderid, String docid,String chatid,String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MESSAGE_COLUMN_ID, mid);
        contentValues.put(MESSAGE_COLUMN_BODY, body);
        contentValues.put(MESSAGE_COLUMN_SENDERID, senderid);
        contentValues.put(MESSAGE_COLUMN_DOCURI, docid);
        contentValues.put(MESSAGE_COLUMN_CHATID, chatid);
        contentValues.put(MESSAGE_COLUMN_TIME, time);
        db.insert(MESSAGE_TABLE_NAME, null, contentValues);
        return true;
    }
    public boolean insertUser (String uid, String uname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, uid);
        contentValues.put(USER_COLUMN_USERNAME, uname);
        contentValues.put(USER_COLUMN_EMAIL, email);
        db.insert(USER_TABLE_NAME, null, contentValues);
        return true;
    }
    public boolean insertChatmember (String cid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHATMEMBER_COLUMN_CHATID, cid);
        db.insert(CHATMEMBER_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getMessagesData(String Chatid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+MESSAGE_TABLE_NAME+" where "+MESSAGE_COLUMN_CHATID+"='"+Chatid+"'", null );
        return res;
    }
    public Cursor getUserData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+USER_TABLE_NAME+" where "+USER_COLUMN_ID +" = '"+id+"'", null );
        return res;
    }
    public Cursor getChatmemberData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+CHATMEMBER_TABLE_NAME+"", null );
        return res;
    }
    public boolean UpdateUserData(String uid,String email, String uname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_USERNAME,uname); //These Fields should be your String values of actual column names
        cv.put(USER_COLUMN_EMAIL,email);
        db.update(USER_TABLE_NAME, cv, USER_COLUMN_ID+"= ?", new String[] { uid });
        return true;
    }

    public int numberOfMessageRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MESSAGE_TABLE_NAME);
        return numRows;
    }
    public int numberOfUserRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
        return numRows;
    }
    public int numberOfChatmemerRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CHATMEMBER_TABLE_NAME);
        return numRows;
    }

    public Integer deleteMessage (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MESSAGE_TABLE_NAME,
                MESSAGE_COLUMN_CHATID+" = ? ",new String[]{id});
    }
    public Integer deleteUser (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME,
                USER_COLUMN_ID+" = ? ",new String[]{id});
    }
    public Integer deleteChatmember (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CHATMEMBER_TABLE_NAME,
                CHATMEMBER_COLUMN_CHATID+" = ? ",new String[]{id});
    }

    public ArrayList<MessageInChat> getAllMessages(String Chatid) {
        ArrayList<MessageInChat> array_list = new ArrayList<MessageInChat>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  getMessagesData(Chatid);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String mid=res.getString(res.getColumnIndex(MESSAGE_COLUMN_ID));
            String body=res.getString(res.getColumnIndex(MESSAGE_COLUMN_BODY));
            String senderid=res.getString(res.getColumnIndex(MESSAGE_COLUMN_SENDERID));
            String time=res.getString(res.getColumnIndex(MESSAGE_COLUMN_TIME));
            String docuri=res.getString(res.getColumnIndex(MESSAGE_COLUMN_DOCURI));
            String chatid=Chatid;
            /*MessageInChat m=new MessageInChat(mid,body,senderid,time,docuri,chatid);
            array_list.add(m);*/
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllChatmember() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  getChatmemberData();
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String cid=res.getString(res.getColumnIndex(CHATMEMBER_COLUMN_CHATID));
            array_list.add(cid);
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<RegisteredUser> getAllUser(String id) {
        ArrayList<RegisteredUser> array_list = new ArrayList<RegisteredUser>();
        Cursor res =  getUserData(id);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String uid=res.getString(res.getColumnIndex(USER_COLUMN_ID));
            String uname=res.getString(res.getColumnIndex(USER_COLUMN_USERNAME));
            String email=res.getString(res.getColumnIndex(USER_COLUMN_EMAIL));
            RegisteredUser u=new RegisteredUser(uname,email,true);
            array_list.add(u);
            res.moveToNext();
        }
        return array_list;
    }
}
