package com.example.library.helper;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_SURNAME = "surname";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_PATRONYMIC = "patronymic";
    private static final String USER_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String USER_COLUMN_BIRTH_DATE = "birth_date";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_ADDRESS = "address";

    private static final String LIBRARY_TABLE_NAME = "library";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + USER_TABLE_NAME +
                " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_USERNAME + " TEXT, " +
                USER_COLUMN_PASSWORD + " TEXT, " +
                USER_COLUMN_SURNAME + " TEXT, " +
                USER_COLUMN_NAME + " TEXT, " +
                USER_COLUMN_PATRONYMIC + " TEXT, " +
                USER_COLUMN_PHONE_NUMBER + " TEXT, " +
                USER_COLUMN_BIRTH_DATE + " DATE, " +
                USER_COLUMN_EMAIL + " TEXT, " +
                USER_COLUMN_ADDRESS + " TEXT" +
                ");";
        db.execSQL(query);
        Log.d(TAG, "Database created"); // Print result to logcat
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    public void addUser(String username, String password, String surname, String name, String patronymic,
                        String phone_number, String birth_date, String email, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_USERNAME, username);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_SURNAME, surname);
        cv.put(USER_COLUMN_NAME, name);
        cv.put(USER_COLUMN_PATRONYMIC, patronymic);
        cv.put(USER_COLUMN_PHONE_NUMBER, phone_number);
        cv.put(USER_COLUMN_BIRTH_DATE, birth_date);
        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_ADDRESS, address);
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Log.d(TAG, "Failed to add user"); // Print result to logcat
        } else {
            Log.d(TAG, "User added"); // Print result to logcat
        }
    }
}
