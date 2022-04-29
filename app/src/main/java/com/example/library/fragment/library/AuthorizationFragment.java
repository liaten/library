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
import com.example.library.helper.AsyncResponse;
import com.example.library.entity.Book;
import com.example.library.helper.DatabaseHelper;
import com.example.library.helper.GetRequestFromDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFragment extends Fragment implements AsyncResponse {

    private EditText LoginEditText, PasswordEditText;
    private LinearLayout AuthorizedLayout;
    private TextView AuthorizedTextView;
    private Button ApplyButton;
    private View view;
    private String user, password, login_app, password_app, name, surname;
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
                try {
                    GetUserFromDB(login_app);
                    GetPasswordByUserFromDB(login_app);

                } catch (IOException e) {
                    e.printStackTrace();
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
            AuthorizedTextView.setText(String.format("%s%s %s", getResources().getString(R.string.authorized), name, surname));
        }
    }
    private void setName(String name, String surname){
        if(name.equals("Гость")){
            AuthorizedLayout.setVisibility(View.GONE);
        }
        else {
            AuthorizedLayout.setVisibility(View.VISIBLE);
            AuthorizedTextView.setText(String.format("%s%s %s", getResources().getString(R.string.authorized), name, surname));
        }
    }

    private void GetUserFromDB(String user) throws IOException {
        new GetRequestFromDatabase(this).execute(user,"username");
    }

    private void GetPasswordByUserFromDB(String user){
        new GetRequestFromDatabase(this).execute(user,"password");
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
        try {
            user = jsonObject.getString("userid");
        } catch (JSONException e) {
            try {
                password = jsonObject.getString("password");
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
            } catch (JSONException jsonException) {
                try {
                    name = jsonObject.getString("name");
                } catch (JSONException exception) {
                    try {
                        surname = jsonObject.getString("surname");
                        if(name!=null){
                            MainActivity.sp.edit().putString("name", name).apply();
                            MainActivity.sp.edit().putString("surname", surname).apply();
                            setName(name, surname);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void GetSurnameFromDB(String login) {
        new GetRequestFromDatabase(this).execute(login,"name");
    }

    private void GetNameFromDB(String login) {
        new GetRequestFromDatabase(this).execute(login,"surname");
    }

}
