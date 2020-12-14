package cn.edu.bistu.cs.se.mywordsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.bistu.cs.se.mywordsapp.contract.notes;


public class NoteDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    /**
     *建表SQL
     * 表： Words.Word.TABLE_NAME
     * 该表中共4个字段：_ID,COLUMN_NAME_WORD,COLUMN_NAME_MEANING,COLUMN_NAME_SAMPLE
     */
    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + notes.Note.TABLE_NAME + " (" +
            notes.Note._ID + " VARCHAR(32) PRIMARY KEY NOT NULL," +
            notes.Note.COLUMN_NAME_TITLE + " TEXT UNIQUE NOT NULL,"+
            notes.Note.COLUMN_NAME_AUTHOR + " TEXT,"
            + notes.Note.COLUMN_NAME_CONTENT + " TEXT)";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + notes.Note.TABLE_NAME;

    public NoteDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
