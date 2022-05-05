package com.example.library.fragment.library;

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
import com.example.library.entity.Book;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.GetRequestFromDatabaseByUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthorizationFragment extends Fragment implements AsyncResponse {

    private EditText LoginEditText, PasswordEditText;
    private LinearLayout AuthorizedLayout;
    private TextView AuthorizedTextView;
    private Button ApplyButton;
    private View view;
    private String user, password, login_app, password_app, name, surname;
    private static final String USERID_PHP_DB = "userid";
    private static final String PASSWORD_PHP_DB = "password";
    private static final String NAME_PHP_DB = "name";
    private static final String SURNAME_PHP_DB = "surname";
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
                GetUserFromDB(login_app);
                GetPasswordByUserFromDB(login_app);
            }
        }
    };

    private void setName(){
        String name = MainActivity.sp.getString("name", "Гость");
        String surname = MainActivity.sp.getString("surname", "");
        setName(name, surname);
    }
    private void setName(String name, String surname){
        if(name!=null && surname!=null){
            if(name.equals("Гость")){
                AuthorizedLayout.setVisibility(View.GONE);
            }
            else {
                AuthorizedLayout.setVisibility(View.VISIBLE);
                AuthorizedTextView.setText(String.format("%s%s %s", getResources().getString(R.string.authorized), name, surname));
            }
        }
        else {
            AuthorizedLayout.setVisibility(View.GONE);
        }

    }

    private void GetUserFromDB(String user) {
        new GetRequestFromDatabaseByUser(this).execute(user,"username");
    }

    private void GetPasswordByUserFromDB(String user){
        new GetRequestFromDatabaseByUser(this).execute(user,"password");
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
    public void returnJSONObject(JSONObject jsonObject) {
        Log.d(TAG, "returnJSONObject: " + jsonObject);
        try {
            if(jsonObject.getBoolean("success")){
                String type = jsonObject.getString("type");
                switch (type){
                    case USERID_PHP_DB:
                        user = jsonObject.getString(USERID_PHP_DB);
                        break;
                    case PASSWORD_PHP_DB:
                        password = jsonObject.getString(PASSWORD_PHP_DB);
                        if(user != null){
                            if(user.equals(login_app) && password.equals(password_app)){
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
                        break;
                    case NAME_PHP_DB:
                        name = jsonObject.getString("name");
                        MainActivity.sp.edit().putString("name", name).apply();
                        setName(name, surname);
                        break;
                    case SURNAME_PHP_DB:
                        surname = jsonObject.getString("surname");
                        MainActivity.sp.edit().putString("surname", surname).apply();
                        setName(name, surname);
                        break;
                }
            }
        } catch (JSONException ignored) {
        }
    }

    private void GetSurnameFromDB(String login) {
        new GetRequestFromDatabaseByUser(this).execute(login,NAME_PHP_DB);
    }

    private void GetNameFromDB(String login) {
        new GetRequestFromDatabaseByUser(this).execute(login,SURNAME_PHP_DB);
    }

}
