package com.example.tripplanner.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tripplanner.database.TravelDB;

public class TravelContentProvider extends ContentProvider {

    // Authority and Content URI for the provider
    public static final String AUTHORITY = "com.example.tripplanner.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");

    // Define constants for the database
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return database != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Implement this method to query data from your database
        return database.query(TravelDB.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Implement this method to return the MIME type of the data at the given URI
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Implement this method to insert data into your database
        long id = database.insert(TravelDB.TABLE_NAME, null, values);
        if (id > 0) {
            return ContentUris.withAppendedId(uri, id);
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Implement this method to delete data from your database
        return database.delete(TravelDB.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        // Implement this method to update data in your database
        return database.update(TravelDB.TABLE_NAME, values, selection, selectionArgs);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, TravelDB.DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TravelDB.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TravelDB.TABLE_NAME);
            onCreate(db);
        }
    }
}
