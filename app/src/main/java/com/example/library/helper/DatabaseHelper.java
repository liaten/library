package com.example.library.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "Library.db";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_SURNAME = "surname";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_PATRONYMIC = "patronymic";
    public static final String USER_COLUMN_PHONE_NUMBER = "phone_number";
    public static final String USER_COLUMN_BIRTH_DATE = "birth_date";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_USERID = "userid";
    public static final String USER_COLUMN_PASSWORD = "password";

    public static final String BOOK_TABLE_NAME = "book";
    public static final String BOOK_COLUMN_ID = "id";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_COVER = "cover";
    public static final String BOOK_THEME = "theme";
    public static final String BOOK_DATE_ADDED = "date";

    public static final String BOOKS_ON_HANDS_TABLE_NAME = "books_on_hands";
    public static final String BOOKS_ON_HANDS_COLUMN_ID = "id";
    public static final String BOOKS_ON_HANDS_COLUMN_USER = "id_user";
    public static final String BOOKS_ON_HANDS_COLUMN_BOOK = "id_book";
    public static final String BOOKS_ON_HANDS_COLUMN_DAYS = "days";

    public static final String RESERVED_BOOKS_TABLE_NAME = "reserved_books";
    public static final String RESERVED_BOOKS_COLUMN_ID = "id";
    public static final String RESERVED_BOOKS_COLUMN_USER = "id_user";
    public static final String RESERVED_BOOKS_COLUMN_BOOK = "id_book";

    public static final String WISHLIST_TABLE_NAME = "wishlist_books";
    public static final String WISHLIST_COLUMN_ID = "id";
    public static final String WISHLIST_COLUMN_USER = "id_user";
    public static final String WISHLIST_COLUMN_BOOK = "id_book";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String query_book = "CREATE TABLE " + BOOK_TABLE_NAME +
                " (" + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOK_TITLE + " TEXT, " +
                BOOK_AUTHOR + " TEXT, " +
                BOOK_COVER + " TEXT, " +
                BOOK_THEME + " TEXT, " +
                BOOK_DATE_ADDED + " DATE" +
                ");";
        String query_user = "CREATE TABLE " + USER_TABLE_NAME +
                " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_SURNAME + " TEXT, " +
                USER_COLUMN_NAME + " TEXT, " +
                USER_COLUMN_PATRONYMIC + " TEXT, " +
                USER_COLUMN_PHONE_NUMBER + " INTEGER, " +
                USER_COLUMN_BIRTH_DATE + " DATE, " +
                USER_COLUMN_EMAIL + " TEXT, " +
                USER_COLUMN_USERID + " INTEGER, " +
                USER_COLUMN_PASSWORD + " TEXT" +
                ");";
        String query_books_on_hands = "CREATE TABLE " + BOOKS_ON_HANDS_TABLE_NAME +
                " (" + BOOKS_ON_HANDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOKS_ON_HANDS_COLUMN_USER + " INTEGER, " +
                BOOKS_ON_HANDS_COLUMN_BOOK + " INTEGER, " +
                BOOKS_ON_HANDS_COLUMN_DAYS + " INTEGER" +
                ");";
        String query_reserved_books = "CREATE TABLE " + RESERVED_BOOKS_TABLE_NAME +
                " (" + RESERVED_BOOKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RESERVED_BOOKS_COLUMN_USER + " INTEGER, " +
                RESERVED_BOOKS_COLUMN_BOOK + " INTEGER" +
                ");";
        String query_wishlist = "CREATE TABLE " + WISHLIST_TABLE_NAME +
                " (" + WISHLIST_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WISHLIST_COLUMN_USER + " INTEGER, " +
                WISHLIST_COLUMN_BOOK + " INTEGER" +
                ");";
        db.execSQL(query_user);
        Log.d(TAG, "Created database " + USER_TABLE_NAME);
        db.execSQL(query_book);
        Log.d(TAG, "Created database " + BOOK_TABLE_NAME);
        db.execSQL(query_books_on_hands);
        Log.d(TAG, "Created database " + BOOKS_ON_HANDS_TABLE_NAME);
        db.execSQL(query_reserved_books);
        Log.d(TAG, "Created database " + RESERVED_BOOKS_TABLE_NAME);
        db.execSQL(query_wishlist);
        Log.d(TAG, "Created database " + WISHLIST_TABLE_NAME);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    public void addUser(@NonNull String surname, @NonNull String name, @NonNull String patronymic,
                        @NonNull long phone_number, @NonNull String birth_date,
                        @NonNull String email, @NonNull int user_id, @NonNull String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_SURNAME, surname);
        cv.put(USER_COLUMN_NAME, name);
        cv.put(USER_COLUMN_PATRONYMIC, patronymic);
        cv.put(USER_COLUMN_PHONE_NUMBER, phone_number);
        cv.put(USER_COLUMN_BIRTH_DATE, birth_date);
        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_USERID, user_id);
        cv.put(USER_COLUMN_PASSWORD, password);
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Log.d(TAG, "Failed to add user"); // Print result to logcat
        } else {
            Log.d(TAG, "User added"); // Print result to logcat
        }
    }
    public void addBook(@NonNull String title, @NonNull String author, @NonNull String cover,
                        @NonNull String theme, @NonNull String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BOOK_TITLE, title);
        cv.put(BOOK_AUTHOR, author);
        cv.put(BOOK_COVER, cover);
        cv.put(BOOK_THEME, theme);
        cv.put(BOOK_DATE_ADDED, date);
        long result = db.insert(BOOK_TABLE_NAME, null, cv);
        if (result == -1) {
            Log.d(TAG, "Failed to add book"); // Print result to logcat
        } else {
            Log.d(TAG, "Book added"); // Print result to logcat
        }
    }

    public Cursor getBooksCount(){
        String query = "SELECT COUNT("+BOOK_COLUMN_ID+") FROM " + BOOK_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor getTopNewBooks(int num){
        String query = "SELECT * FROM " + BOOK_TABLE_NAME + " ORDER BY " + BOOK_DATE_ADDED
                + " desc LIMIT " + num;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public static String getSQLDate(int day, int month, int year) {
        String dayStr, monthStr, yearStr;
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = String.valueOf(day);
        }
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = String.valueOf(month);
        }
        if (year < 10) {
            yearStr = "000" + year;
        } else if (year < 100) {
            yearStr = "00" + year;
        } else if (year < 1000) {
            yearStr = "0" + year;
        } else {
            yearStr = String.valueOf(year);
        }
        Log.d(TAG, "SQLDATE: " + yearStr + "-" + monthStr + "-" + dayStr);
        return yearStr + "-" + monthStr + "-" + dayStr;
    }
}
