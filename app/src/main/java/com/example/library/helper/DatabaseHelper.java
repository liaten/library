package com.example.library.helper;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private  static final String COLUMN_PASSWORD = "password";
    private  static final String COLUMN_SURNAME = "surname";
    private  static final String COLUMN_NAME = "name";
    private  static final String COLUMN_PATRONYMIC = "patronymic";
    private  static final String COLUMN_PHONE_NUMBER = "phone_number";
    private  static final String COLUMN_BIRTH_DATE = "birth_date";
    private  static final String COLUMN_EMAIL = "email";
    private  static final String COLUMN_ADDRESS = "address";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_SURNAME + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PATRONYMIC + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_BIRTH_DATE + " DATE, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ADDRESS + " TEXT" +
                ");";
        db.execSQL(query);
        Log.d(TAG,"Database created"); // Print result to logcat
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addUser(String username, String password, String surname, String name, String patronymic,
                 String phone_number, String birth_date, String email, String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME,username);
        cv.put(COLUMN_PASSWORD,password);
        cv.put(COLUMN_SURNAME,surname);
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_PATRONYMIC,patronymic);
        cv.put(COLUMN_PHONE_NUMBER,phone_number);
        cv.put(COLUMN_BIRTH_DATE,birth_date);
        cv.put(COLUMN_EMAIL,email);
        cv.put(COLUMN_ADDRESS,address);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Log.d(TAG,"Failed to add user"); // Print result to logcat
        }else {
            Log.d(TAG,"User added"); // Print result to logcat
        }
    }
}
