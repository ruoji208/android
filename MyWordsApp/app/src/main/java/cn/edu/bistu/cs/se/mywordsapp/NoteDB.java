package cn.edu.bistu.cs.se.mywordsapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.bistu.cs.se.mywordsapp.contract.notes;


public class NoteDB {
    private static final String TAG = "myTag";

    private static NoteDBHelper mDbHelper;

    //采用单例模式
    private static NoteDB instance=new NoteDB();
    public static NoteDB getWordsDB(){
        return NoteDB.instance;
    }

    private NoteDB() {
        if (mDbHelper == null) {
            mDbHelper = new NoteDBHelper(Application.getContext());
        }
    }


    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }

    //获得单个单词的全部信息
    public notes.WordDescription getSingleWord(String id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where _ID=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            ;
            notes.WordDescription item = new notes.WordDescription(cursor.getString(cursor.getColumnIndex(notes.Note._ID)),
                    cursor.getString(cursor.getColumnIndex(notes.Note.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(notes.Note.COLUMN_NAME_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(notes.Note.COLUMN_NAME_CONTENT)));
            return item;
        }
        return null;

    }

    //得到全部单词列表
    public ArrayList<Map<String, String>> getAllWords() {
        if (mDbHelper == null) {
            Log.v(TAG, "WordsDB::getAllWords()");
            return null;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                notes.Note._ID,
                notes.Note.COLUMN_NAME_TITLE
        };

        //排序
        String sortOrder =
                notes.Note.COLUMN_NAME_TITLE + " DESC";


        Cursor c = db.query(
                notes.Note.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2WordList(c);
    }



    //将游标转化为单词列表
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(notes.Note._ID, String.valueOf(cursor.getString(cursor.getColumnIndex(notes.Note._ID))));
            map.put(notes.Note.COLUMN_NAME_TITLE, cursor.getString(cursor.getColumnIndex(notes.Note.COLUMN_NAME_TITLE)));
            result.add(map);
        }
        return result;
    }

    //使用Sql语句插入单词
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {
        String sql = "insert into  words(_id,word,meaning,sample) values(?,?,?,?)";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{GUID.getGUID(),strWord, strMeaning, strSample});
    }

    //使用insert方法增加单词
    public void Insert(String strWord, String strMeaning, String strSample) {

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(notes.Note._ID, GUID.getGUID());
        values.put(notes.Note.COLUMN_NAME_TITLE, strWord);
        values.put(notes.Note.COLUMN_NAME_AUTHOR, strMeaning);
        values.put(notes.Note.COLUMN_NAME_CONTENT, strSample);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                notes.Note.TABLE_NAME,
                null,
                values);
    }


    //使用Sql语句删除单词
    public void DeleteUseSql(String strId) {
        String sql = "delete from words where _id='" + strId + "'";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }

    //删除单词
    public void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = notes.Note._ID + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};

        // Issue SQL statement.
        db.delete(notes.Note.TABLE_NAME, selection, selectionArgs);
    }


    //使用Sql语句更新单词
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord,strMeaning, strSample, strId});
    }

    //使用方法更新
    public void Update(String strId, String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(notes.Note.COLUMN_NAME_TITLE, strWord);
        values.put(notes.Note.COLUMN_NAME_AUTHOR, strMeaning);
        values.put(notes.Note.COLUMN_NAME_CONTENT, strSample);

        String selection = notes.Note._ID + " = ?";
        String[] selectionArgs = {strId};

        int count = db.update(
                notes.Note.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    //使用Sql语句查找
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "select * from words where word like ? order by word desc";
        Cursor c = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});

        return ConvertCursor2WordList(c);
    }

    //使用query方法查找
    public ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                notes.Note._ID,
                notes.Note.COLUMN_NAME_TITLE
        };

        String sortOrder =
                notes.Note._ID + " DESC";

        String selection = notes.Note._ID + " LIKE ?";
        String[] selectionArgs = {"%" + strWordSearch + "%"};

        Cursor c = db.query(
                notes.Note.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2WordList(c);
    }


}
