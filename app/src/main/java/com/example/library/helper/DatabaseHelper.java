package com.example.library.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "Library.db";
    private static final int DATABASE_VERSION = 1;

    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_SURNAME = "surname";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_PATRONYMIC = "patronymic";
    private static final String USER_COLUMN_PHONE_NUMBER = "phone_number";
    private static final String USER_COLUMN_BIRTH_DATE = "birth_date";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_USERID = "userid";
    private static final String USER_COLUMN_PASSWORD = "password";

    private static final String BOOK_TABLE_NAME = "book";
    private static final String BOOK_COLUMN_ID = "id";
    private static final String BOOK_TITLE = "title";
    private static final String BOOK_AUTHOR = "author";
    private static final String BOOK_COVER = "cover";
    private static final String BOOK_THEME = "theme";

    private static final String BOOKS_ON_HANDS_TABLE_NAME = "books_on_hands";
    private static final String BOOKS_ON_HANDS_COLUMN_ID = "id";
    private static final String BOOKS_ON_HANDS_COLUMN_USER = "id_user";
    private static final String BOOKS_ON_HANDS_COLUMN_BOOK = "id_book";
    private static final String BOOKS_ON_HANDS_COLUMN_DAYS = "days";

    private static final String RESERVED_BOOKS_TABLE_NAME = "reserved_books";
    private static final String RESERVED_BOOKS_COLUMN_ID = "id";
    private static final String RESERVED_BOOKS_COLUMN_USER = "id_user";
    private static final String RESERVED_BOOKS_COLUMN_BOOK = "id_book";

    private static final String WISHLIST_TABLE_NAME = "wishlist_books";
    private static final String WISHLIST_COLUMN_ID = "id";
    private static final String WISHLIST_COLUMN_USER = "id_user";
    private static final String WISHLIST_COLUMN_BOOK = "id_book";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String query_book = "CREATE TABLE " + BOOK_TABLE_NAME +
                " (" + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BOOK_TITLE + " TEXT, " +
                BOOK_AUTHOR + " TEXT, " +
                BOOK_COVER + " BLOB" +
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
    public void addBook(@NonNull String title, @NonNull String author, @NonNull byte[] cover) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BOOK_TITLE, title);
        cv.put(BOOK_AUTHOR, author);
        cv.put(BOOK_COVER, cover);
        long result = db.insert(BOOK_TABLE_NAME, null, cv);
        if (result == -1) {
            Log.d(TAG, "Failed to add user"); // Print result to logcat
        } else {
            Log.d(TAG, "User added"); // Print result to logcat
        }
    }
}
