package com.example.library.fragment.library;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.Book;
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.Password;
import com.example.library.helper.Username;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AuthorizationFragment extends Fragment implements AsyncResponse {

    private EditText LoginEditText, PasswordEditText;
    private LinearLayout AuthorizedLayout;
    private TextView AuthorizedTextView;
    private Button ApplyButton;
    private View view;
    private DatabaseHelper db;
    private String user, password, login_app, password_app;
    private static final String TAG = "AuthorizationFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authorization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
        setName();
    }

    private void setOnClickListeners() {
        ApplyButton.setOnClickListener(applyButtonListener);
    }

    private void setViews() {
        view = requireView();
        LoginEditText = view.findViewById(R.id.login_edit_text);
        PasswordEditText = view.findViewById(R.id.password_edit_text);
        ApplyButton = view.findViewById(R.id.auth_ok);
        AuthorizedTextView = view.findViewById(R.id.authorized_text_view);
        AuthorizedLayout = view.findViewById(R.id.authorized_layout);
        db = new DatabaseHelper(view.getContext());
    }

    private final View.OnClickListener applyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login_app = LoginEditText.getText().toString().trim();
            password_app = PasswordEditText.getText().toString().trim();
            if(login_app.equals("") || password_app.equals("")){
                Toast.makeText(requireActivity(),
                        "Данные не введены",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    GetUserFromDB(login_app);
                    GetPasswordByUserFromDB(login_app);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                try {
//                    if(GetUserFromDB(login).equals(login) && GetPasswordByUserFromDB(login).equals(password)){
//                        MainActivity.sp.edit().putBoolean("logged",true).apply();
//                        MainActivity.sp.edit().putString("login", login).apply();
//                        MainActivity.sp.edit().putString("name", GetNameFromDB(login)).apply();
//                        MainActivity.sp.edit().putString("surname", GetSurnameFromDB(login)).apply();
//                        Toast.makeText(requireActivity(),
//                                "Успешный вход",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(requireActivity(),
//                                "Неверные данные ввода",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    };

    private void setName(){
        String name = MainActivity.sp.getString("name", "Гость");
        String surname = MainActivity.sp.getString("surname", "");
        if(name.equals("Гость")){
            AuthorizedLayout.setVisibility(View.GONE);
        }
        else {
            AuthorizedLayout.setVisibility(View.VISIBLE);
            AuthorizedTextView.setText(AuthorizedTextView.getText() + name + surname);
        }
    }

    private void GetUserFromDB(String user) throws IOException {
        new Username(this).execute(user);
    }

    private String GetNameFromDB(String user){
        Cursor cursor = db.searchName(user);
        String name_from_db = "";
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                name_from_db = cursor.getString(0);
            }
        }
        return name_from_db;
    }

    private String GetSurnameFromDB(String user){
        Cursor cursor = db.searchSurname(user);
        String surname = "";
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                surname = cursor.getString(0);
            }
        }
        return surname;
    }

    private void GetPasswordByUserFromDB(String user){
        new Password(this).execute(user);
    }

    @Override
    public void processFinish(Boolean output) {
        //
    }

    @Override
    public void returnBooks(ArrayList<Book> output) {
//
    }

    @Override
    public void processFinish(Bitmap output) {
//
    }

    @Override
    public void returnUser(String user) {
        this.user = user;
        Log.d(TAG, "returnUser: " + user);
    }

    @Override
    public void returnPassword(String password) {
        this.password = password;
        Log.d(TAG, "returnPassword: " + password);
        if(this.user.equals(login_app) && this.password.equals(password_app)){
            MainActivity.sp.edit().putBoolean("logged",true).apply();
            MainActivity.sp.edit().putString("login", login_app).apply();
            GetNameFromDB(login_app);
            GetSurnameFromDB(login_app);
            Toast.makeText(requireActivity(),
                    "Успешный вход",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(requireActivity(),
                    "Неверные данные ввода",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void returnName(String name) {
        MainActivity.sp.edit().putString("name", name).apply();
    }

    @Override
    public void returnSurname(String surname) {
        MainActivity.sp.edit().putString("name", surname).apply();
    }

}
