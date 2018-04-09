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

public class LocalDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Hermes.db";
    public static final String MESSAGE_TABLE_NAME = "Messages";
    public static final String MESSAGE_COLUMN_ID = "MessID";
    public static final String MESSAGE_COLUMN_BODY = "Body";
    public static final String MESSAGE_COLUMN_SENDERID = "SenderID";
    public static final String MESSAGE_COLUMN_DOCURI = "DocURI";
    public static final String MESSAGE_COLUMN_CHATID = "ChatID";
    public static final String MESSAGE_COLUMN_TIME = "Time";
    private HashMap hp;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+MESSAGE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMessage (String mid, String body, String senderid, String docid,String chatid,String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MessID", mid);
        contentValues.put("Body", body);
        contentValues.put("SenderID", senderid);
        contentValues.put("DocURI", docid);
        contentValues.put("ChatID", chatid);
        contentValues.put("Time", time);
        db.insert("Messages", null, contentValues);
        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Message where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MESSAGE_TABLE_NAME);
        return numRows;
    }

    public Integer deleteContact (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("messages",
                "id = ? ",new String[]{id});
    }

    public ArrayList<MessageInChat> getAllMessages(String id) {
        ArrayList<MessageInChat> array_list = new ArrayList<MessageInChat>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  getData(id);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String mid=res.getString(res.getColumnIndex(MESSAGE_COLUMN_ID));
            String body=res.getString(res.getColumnIndex(MESSAGE_COLUMN_BODY));
            String senderid=res.getString(res.getColumnIndex(MESSAGE_COLUMN_SENDERID));
            String time=res.getString(res.getColumnIndex(MESSAGE_COLUMN_TIME));
            String docuri=res.getString(res.getColumnIndex(MESSAGE_COLUMN_DOCURI));
            String chatid=id;
            /*MessageInChat m=new MessageInChat(mid,body,senderid,time,docuri,chatid);
            array_list.add(m);*/
            res.moveToNext();
        }
        return array_list;
    }
}
