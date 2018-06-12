package com.example.r4z0r78.diaryapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TD11 on 08/08/2017.
 */
public class NotesDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "noteDB";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE_NAME_NOTE = "Note";
    private static final String NOTE_COLUMN_ID = "id";
    private static final String NOTE_COLUMN_TITLE = "title";
    private static final String NOTE_COLUMN_CONTENT = "content";

    public NotesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "Create Table " + DB_TABLE_NAME_NOTE +
                        "(" + NOTE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NOTE_COLUMN_TITLE + " TEXT, " +
                        NOTE_COLUMN_CONTENT + " TEXT);";

        // execute
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "Drop table " + DB_TABLE_NAME_NOTE;

        db.execSQL(query);

        // recreate the table
        onCreate(db);
    }

    public void saveNote(Note n) throws SQLiteException{
        // Create an instance of content values

        // TODO 1: use try/catch/finally to be sure that the connection is actually close whatever happens (throw an exception on the catch)

        // Create an instance of sqlitedatabase
        SQLiteDatabase db = getWritableDatabase();

        try{
            ContentValues cv = new ContentValues();
            // put a note
            cv.put(NOTE_COLUMN_TITLE, n.getTitle());
            cv.put(NOTE_COLUMN_CONTENT, n.getText());

            db.insert(DB_TABLE_NAME_NOTE, null, cv);

        }catch (SQLiteException ex){
            throw ex;
        }finally {
            db.close();
        }


    }

    public List<Note> getNotes() throws SQLiteException{

        List<Note> ret = new ArrayList<Note>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cr = null;

        try{
            // create a cursor to read notes
            cr = db.query(DB_TABLE_NAME_NOTE, new String[]{NOTE_COLUMN_ID, NOTE_COLUMN_TITLE, NOTE_COLUMN_CONTENT}, null, null, null, null, null);

            if (cr != null){
                if (cr.moveToFirst()){
                    do{
                        // get the variables
                        
                        int id = cr.getInt(cr.getColumnIndex(NOTE_COLUMN_ID));
                        String title = cr.getString(cr.getColumnIndex(NOTE_COLUMN_TITLE));
                        String text = cr.getString(cr.getColumnIndex(NOTE_COLUMN_CONTENT));

                        // Create an instance of a Note
                        Note n = new Note();
                        n.setId(id);
                        n.setText(text);
                        n.setTitle(title);

                        // Add the note to the array

                        ret.add(n);

                    }while (cr.moveToNext());
                }
            }
        }catch (SQLiteException ex){
            throw ex;
        }finally{

            if (cr != null){
                cr.close();
            }
            db.close();
        }

        return ret;
    }
}
