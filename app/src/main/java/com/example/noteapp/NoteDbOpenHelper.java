package com.example.noteapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.noteapp.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "noteSQLite.db";
    private static final String TABLE_NAME_NOTE = "note";

    //Account Table
//    public static String Title ="title";
//    public static String ID = "id";
//    public static String Content = "content";
//    public static String Create_time = "create_time";

//    private static String CREATE_TABLE_SQL = "create table " + TABLE_NAME_NOTE + " ( " + ID + " integer primary key autoincrement, "
//            + Title + " text not null, "
//            + Content + " text, "
//            + Create_time + " text)";
    private static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME_NOTE + " (id integer primary key autoincrement, title text, content text, create_time text)";


    public NoteDbOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertData(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("create_time",note.getCreatedTime());

        Log.e("db_row", String.valueOf(values));

        return db.insert(TABLE_NAME_NOTE,null, values);
    }

    public int deleteFromDbById(String id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME_NOTE, "id like ?", new String[]{id});
    }

    public int updateData(Note note) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("create_time",note.getCreatedTime());

        Log.e("db_row_update", String.valueOf(values));

        //id是哪個更新那個 is =
        return db.update(TABLE_NAME_NOTE, values,  "id like ?", new String[]{note.getId()});
    }

    public List<Note> queryAllFromDB() {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        //Cursor可稱為資料指標，要查詢某一筆紀錄必須將Cursor指標指到它，才能讀取其內容
        //查詢所有，不需要條件
        Cursor cursor = db.query(TABLE_NAME_NOTE,null,null,null,null,null,null,null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String createTime = cursor.getString(cursor.getColumnIndexOrThrow("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title);
                note.setContent(content);
                note.setCreatedTime(createTime);

                noteList.add(note);
            }
            cursor.close();
        }

        return noteList;
    }

    public List<Note> queryFromDbByTitle(String title) {
        if(TextUtils.isEmpty(title)) {
            return queryAllFromDB();
        }
        SQLiteDatabase db = getWritableDatabase();
        List<Note> noteList = new ArrayList<>();

        // "%"+title+"%" 模糊匹配
        Cursor cursor = db.query(TABLE_NAME_NOTE, null, "title like ?", new String[]{"%"+title+"%"}, null, null, null);

        if(cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String title2 = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String createTime = cursor.getString(cursor.getColumnIndexOrThrow("create_time"));

                Note note = new Note();
                note.setId(id);
                note.setTitle(title2);
                note.setContent(content);
                note.setCreatedTime(createTime);

                noteList.add(note);
            }
            cursor.close();
        }
    }
}
