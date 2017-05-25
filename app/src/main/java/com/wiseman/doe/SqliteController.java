package com.wiseman.doe;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Student on 10/7/2016.
 */
public class SqliteController extends SQLiteOpenHelper
{
    private static final String LOGCAT = null;
    public SqliteController(Context applicationcontext)
    {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT,"Created");
    }
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String query,query1;
        query = "CREATE TABLE IF NOT EXISTS Messages (messageId INTEGER PRIMARY KEY, message TEXT,subject TEXT,link TEXT,author TEXT,date TEXT,filename TEXT,urgent TEXT,attach TEXT)";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version)
    {
        String query;
        query = "DROP TABLE IF EXISTS Messages";
        database.execSQL(query);
        onCreate(database);
    }
    public void insertMessage(int messageId,String message,String subject,String author,String link,String date,String filename,String urgent,String attach)
    {
        Log.d(LOGCAT,"insert");
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "INSERT INTO Messages (messageId,message,subject,link,author,date,filename,urgent,attach) VALUES("+messageId+",'"+message+"','"+subject+"', '"+link+"','"+author+"','"+date+"','"+filename+"','"+urgent+"','"+attach+"');";
        Log.d("query",query);
        database.execSQL(query);
        database.close();
    }

    public void deleteMessage(int id)
    {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Messages where messageId='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }
    public Cursor getAllMessages()
    {
        String selectQuery = "SELECT * FROM Messages";
        String []columns = {"messageId","message","subject","link","author","date","filename","urgent","attach"};
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("Messages",columns, null,null,null,null,"date DESC");

        return cursor;
    }

}
