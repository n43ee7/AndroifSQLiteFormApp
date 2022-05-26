package com.napps.project;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import android.text.TextUtils;
import android.util.Log;
public class PersonManagerContentProvider extends ContentProvider {
    /* Declare and initialize constants and variables */
    public static final String _ID = "id";
    public static final String NAME = "name";
    private static final String TAG = "just";
    private static final String DATABASE_NAME = "Person.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "person";
    public static final String PROVIDER_NAME= "edu.just.provider.person";
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + PROVIDER_NAME + "/person");
    /*
     * Declare a constant to identify a URI that returns all records in the
     * person table
     */
    private static final int PERSONALL = 1;
    /*
     * Declare a constant to identify a URI that returns a record at a specific
     * ID in a table
     */
    private static final int PERSON_ID = 2;
    private SQLiteDatabase personDB;
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
            Log.w("Example",
                    "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    private OpenHelper dbHelper;
    @Override
    public boolean onCreate() {
        Context context = getContext();
        OpenHelper dbHelper = new OpenHelper(context);
        personDB = dbHelper.getWritableDatabase();
        return personDB != null;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(TABLE_NAME);
        if (uriMatcher.match(uri) == PERSON_ID)
            // ---if getting a particular book---
            sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
        Cursor c = sqlBuilder.query(personDB, projection, selection, selectionArgs,
                null, null,
                sortOrder);
        // ---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // ---get all reviews---
            case PERSONALL:
                return "vnd.android.cursor.dir/vnd.personmanager.person";
            // ---get a particular review---
            case PERSON_ID:
                return "vnd.android.cursor.item/vnd.personmanager.person";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = personDB.insert(TABLE_NAME, "", values);
        // ---if added successfully---
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        /*
         * Check whether the request is for deleting all records or a single
         * record. Accordingly take the appropriate action.
         */
        switch (uriMatcher.match(uri)) {
            case PERSONALL:
                count = personDB.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case PERSON_ID:
                String id = uri.getPathSegments().get(1);
                count = personDB.delete(TABLE_NAME,_ID + " = " + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                        : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PERSONALL:
                count = personDB.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case PERSON_ID:
                count = personDB.update(TABLE_NAME, values, _ID + " = "
                                + uri.getPathSegments().get(1)
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection +
                                ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        /* Notify registered observers that a row was updated */
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    /* Add available URIs to the URIMatcher object */
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME, PERSONALL);
        uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME+ "/#", PERSON_ID);
    }
}