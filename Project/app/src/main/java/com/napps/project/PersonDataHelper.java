package com.napps.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;
public class PersonDataHelper {
    /* Variable initializations */
    private static final String DATABASE_NAME = "Person.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "person";
    private Context context;
    private SQLiteDatabase db;
    /* Create an SQLiteStatement object to insert a record into the table */
    private SQLiteStatement insertStmt;
    /* Create a string constant that contains the insert statement */
    private static final String INSERT = "insert into "
            + TABLE_NAME
            + "(id, name, birth, sex, interests, chineselevel, workexp) values(?,?,?,?,?,?,?)";
    /* Constructor */
    public PersonDataHelper(Context context) {
        this.context = context;
        /* Open the database and create a compiled insert statement */
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }
    /* Insert method */
    public long insert(String id, String name, String birth, String sex,String
            interests,
                       double chineseLevel, String workexp) throws
            SQLiteConstraintException {
        /* Bind actual values to the question marks in the insert statement */
        this.insertStmt.bindString(1,id);
        this.insertStmt.bindString(2, name);
        this.insertStmt.bindString(3, birth);
        this.insertStmt.bindString(4, sex);
        this.insertStmt.bindString(5, interests);
        this.insertStmt.bindDouble(6, chineseLevel);
        this.insertStmt.bindString(7, workexp);
        return this.insertStmt.executeInsert();
    }
    /* Method to return the record with a specific id */
    public List<String> selectById(String id) {
        List<String> list = new ArrayList<String>();
        /*
         * Query the table to retrieve the record with the specified id
         */
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "id","name", "birth", "sex", "interests", "chineselevel", "workexp" }, "id=" + id, null, null, null, null);
        /*
         * If the cursor is not empty, add the retrieved record to a List
         * variable
         */
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(0) + ";" + cursor.getString(1) + ";"
                    + cursor.getString(2) + ";" + cursor.getString(3) + ";"
                    + cursor.getString(4) + ";" + cursor.getDouble(5) + ";"
                    + cursor.getString(6));
        }
        /* Close the cursor */
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    /* Method to return the names of all the person */
    public List<String> selectName() {
        List<String> list = new ArrayList<String>();
        /* Query the table to retrieve the names of all movies */
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name" },
                null, null, null, null, null);
        /*
         * If the cursor is not empty, add all retrieved records to the List
         * variable
         */
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        /* Close the cursor */
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    public List<Person> selectIdAndName() {
        List<Person> list = new ArrayList<Person>();
        /* Query the table to retrieve the names of all movies */
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "id","name" },
                null, null, null, null, null);
        /*
         * If the cursor is not empty, add all retrieved records to the List
         * variable
         */
        if (cursor.moveToFirst()) {
            do {
                list.add(new Person(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        /* Close the cursor */
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
    /* Create a class by extending the SQLiteOpenHelper class */
    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                            + TABLE_NAME
                            + "(id TEXT PRIMARY KEY, name TEXT, birth TEXT, sex TEXT, interests TEXT, chineselevel DOUBLE, workexp TEXT)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.w("Example",
            // "Upgrading database, this will drop tables and recreate.");
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            //onCreate(db);
        }
    }
    public List<String> selectByName(String name) {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<String>();
        /*
         * Query the table to retrieve the record with the specified id
         */
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "id","name","birth",
                        "sex", "interests", "chineselevel", "workexp" }, "name=?" ,new String[]{name}
                , null, null, null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(0) + ";" + cursor.getString(1) + ";"
                    + cursor.getString(2) + ";" + cursor.getString(3) + ";"
                    + cursor.getString(4) + ";" + cursor.getDouble(5) + ";"
                    + ";" + cursor.getString(6));
        }
        /* Close the cursor */
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}