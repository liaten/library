package com.example.library.fragment.library;

import android.graphics.Bitmap;
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
import com.example.library.entity.Book;
import com.example.library.entity.Event;
import com.example.library.helper.AsyncResponse;
import com.example.library.helper.GetRequestFromDatabaseByUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthorizationFragment extends Fragment implements AsyncResponse {

    private EditText LoginEditText, PasswordEditText;
    private LinearLayout AuthorizedLayout, LoginActive, LoginInactive;
    private TextView AuthorizedTextView;
    private Button ApplyButton, LogoutButton;
    private View view;
    private String user, password, login_app, password_app, name, surname, email;
    private static final String USERID_PHP_DB = "userid";
    private static final String PASSWORD_PHP_DB = "password";
    private static final String EMAIL_PHP_DB = "email";
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
        LogoutButton.setOnClickListener(logoutButtonListener);
    }

    private void setViews() {
        view = requireView();
        LoginEditText = view.findViewById(R.id.login_edit_text);
        PasswordEditText = view.findViewById(R.id.password_edit_text);
        ApplyButton = view.findViewById(R.id.auth_ok);
        LogoutButton = view.findViewById(R.id.logout);
        AuthorizedTextView = view.findViewById(R.id.authorized_text_view);
        AuthorizedLayout = view.findViewById(R.id.authorized_layout);
        LoginActive = view.findViewById(R.id.login_active);
        LoginInactive = view.findViewById(R.id.login_inactive);
    }

    private final View.OnClickListener applyButtonListener = view -> {
        login_app = LoginEditText.getText().toString().trim();
        password_app = PasswordEditText.getText().toString().trim();
        if(login_app.equals("") || password_app.equals("")){
            Toast.makeText(requireActivity(),
                    "Данные не введены",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            GetUserFromDB(login_app);
        }
    };

    private final View.OnClickListener logoutButtonListener = view -> {
        MainActivity.getSP().edit().putBoolean("logged",false).apply();
        MainActivity.getSP().edit().putString("email", null).apply();
        MainActivity.getSP().edit().putString("userid", null).apply();
        MainActivity.getSP().edit().putString("password", null).apply();
        MainActivity.getSP().edit().putString(NAME_PHP_DB, null).apply();
        MainActivity.getSP().edit().putString(SURNAME_PHP_DB, null).apply();
        setName();
    };

    private void setName(){
        String name = MainActivity.getSP().getString(NAME_PHP_DB, "Гость");
        String surname = MainActivity.getSP().getString(SURNAME_PHP_DB, "");
        setName(name, surname);
    }
    private void setName(String name, String surname){
        if(name!=null && surname!=null){
            if(name.equals("Гость")){
                AuthorizedLayout.setVisibility(View.GONE);
                LoginActive.setVisibility(View.VISIBLE);
                LoginInactive.setVisibility(View.GONE);
            }
            else {
                AuthorizedLayout.setVisibility(View.VISIBLE);
                AuthorizedTextView.setText(String.format("%s%s %s", getResources().getString(R.string.authorized), name, surname));
                LoginActive.setVisibility(View.GONE);
                LoginInactive.setVisibility(View.VISIBLE);
            }
        }
        else {
            AuthorizedLayout.setVisibility(View.GONE);
            LoginActive.setVisibility(View.VISIBLE);
            LoginInactive.setVisibility(View.GONE);

        }

    }

    private void GetUserFromDB(String user) {
        new GetRequestFromDatabaseByUser(this).execute(USERID_PHP_DB,USERID_PHP_DB,user);
    }

    private void GetPasswordByUserFromDB(String user){
        new GetRequestFromDatabaseByUser(this).execute(PASSWORD_PHP_DB,USERID_PHP_DB, user);
    }

    private void GetEmailFromDB(String email) {
        new GetRequestFromDatabaseByUser(this).execute(EMAIL_PHP_DB,EMAIL_PHP_DB,email);
    }

    private void GetEmailFromDBByUserID(String userid) {
        new GetRequestFromDatabaseByUser(this).execute(EMAIL_PHP_DB,USERID_PHP_DB,userid);
    }

    private void GetPasswordByEmailFromDB(String email){
        new GetRequestFromDatabaseByUser(this).execute(PASSWORD_PHP_DB,EMAIL_PHP_DB, email);
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
    public void returnEvents(ArrayList<Event> output) {

    }

    @Override
    public void returnBooks(ArrayList<Book> output, String table) {

    }

    @Override
    public void returnTable(String table) {

    }

    @Override
    public void processFinish(Bitmap output) {
//
    }

    @Override
    public void processFinish(Bitmap output, String table) {

    }

    @Override
    public void returnJSONObject(JSONObject jsonObject) {
//        Log.d(TAG, "returnJSONObject: " + jsonObject);
        try {
            if (jsonObject.getBoolean("success")) {
                String type = jsonObject.getString("type");
                switch (type) {
                    case USERID_PHP_DB:
                        user = jsonObject.getString(USERID_PHP_DB);
                        if(email==null){
                            GetPasswordByUserFromDB(user);
                        }
                        else {
                            MainActivity.getSP().edit().putString("userid", user).apply();
                        }

                        break;
                    case PASSWORD_PHP_DB:
                        password = jsonObject.getString(PASSWORD_PHP_DB);
                        if(user != null){
                            if(user.equals(login_app) && password.equals(password_app)){
                                int auths_num = MainActivity.getSP().getInt("auths_num", 0) + 1;
                                MainActivity.getSP().edit().putInt("auths_num", auths_num).apply();

                                MainActivity.getSP().edit().putBoolean("logged",true).apply();
                                MainActivity.getSP().edit().putString("userid", login_app).apply();
                                MainActivity.getSP().edit().putString("password", password).apply();
                                GetNameFromDB(login_app);
                                GetSurnameFromDB(login_app);
                                GetEmailFromDBByUserID(login_app);
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
                        else {
                            if(email.equals(login_app) && password.equals(password_app)){
                                int auths_num = MainActivity.getSP().getInt("auths_num", 0) + 1;
                                MainActivity.getSP().edit().putInt("auths_num", auths_num).apply();
                                MainActivity.getSP().edit().putBoolean("logged",true).apply();
                                MainActivity.getSP().edit().putString("email", login_app).apply();
                                MainActivity.getSP().edit().putString("password", password).apply();
                                GetNameFromDBByEmail(login_app);
                                GetSurnameFromDBByEmail(login_app);
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
                        name = jsonObject.getString(NAME_PHP_DB);
                        MainActivity.getSP().edit().putString(NAME_PHP_DB, name).apply();
                        setName(name, surname);
                        break;
                    case SURNAME_PHP_DB:
                        surname = jsonObject.getString(SURNAME_PHP_DB);
                        MainActivity.getSP().edit().putString(SURNAME_PHP_DB, surname).apply();
                        setName(name, surname);
                        break;
                    case EMAIL_PHP_DB:
                        email = jsonObject.getString(EMAIL_PHP_DB);
                        if(user==null){
                            GetPasswordByEmailFromDB(email);
                        }
                        else {
                            MainActivity.getSP().edit().putString("email", login_app).apply();
                        }

                        break;
                }
            }
            else {
                String supposed = jsonObject.getString("supposed_to_be");
                if(supposed.equals(USERID_PHP_DB)){
                    user = null;
                    String typeValue = jsonObject.getString("typeValue");
                    GetEmailFromDB(typeValue);
                }
            }
        } catch (JSONException ignored) {
        }
    }

    private void GetSurnameFromDB(String userid) {
        new GetRequestFromDatabaseByUser(this).execute(SURNAME_PHP_DB, USERID_PHP_DB, userid);
    }

    private void GetSurnameFromDBByEmail(String email) {
        new GetRequestFromDatabaseByUser(this).execute(SURNAME_PHP_DB, EMAIL_PHP_DB, email);
    }

    private void GetNameFromDB(String userid) {
        new GetRequestFromDatabaseByUser(this).execute(NAME_PHP_DB, USERID_PHP_DB, userid);
    }

    private void GetNameFromDBByEmail(String email) {
        new GetRequestFromDatabaseByUser(this).execute(NAME_PHP_DB, EMAIL_PHP_DB, email);
    }

}
