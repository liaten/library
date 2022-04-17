package com.example.library.fragment.library;

import android.database.Cursor;
import android.os.Bundle;
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
import com.example.library.helper.DatabaseHelper;

public class AuthorizationFragment extends Fragment {

    private EditText LoginEditText, PasswordEditText;
    private LinearLayout AuthorizedLayout;
    private TextView AuthorizedTextView;
    private Button ApplyButton;
    private View view;
    private DatabaseHelper db;
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
            String login = LoginEditText.getText().toString().trim();
            String password = PasswordEditText.getText().toString().trim();
            if(login.equals("") || password.equals("")){
                Toast.makeText(requireActivity(),
                        "Данные не введены",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if(GetUserFromDB(login).equals(login) && GetPasswordByUserFromDB(login).equals(password)){
                    MainActivity.sp.edit().putBoolean("logged",true).apply();
                    MainActivity.sp.edit().putString("login", login).apply();
                    MainActivity.sp.edit().putString("name", GetNameFromDB(login)).apply();
                    MainActivity.sp.edit().putString("surname", GetSurnameFromDB(login)).apply();
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

    private String GetUserFromDB(String user){
        Cursor cursor = db.searchUser(user);
        String user_from_db = "";
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                user_from_db = cursor.getString(0);
            }
        }
        return user_from_db;
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

    private String GetPasswordByUserFromDB(String user){
        Cursor cursor = db.searchPassword(user);
        String password_from_db = "";
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                password_from_db = cursor.getString(0);
            }
        }
        return password_from_db;
    }
}
