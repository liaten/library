package com.example.library.fragment.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;

public class StatisticsFragment extends Fragment {

    private View view;
    private TextView FIOTextView, LoginTextView, EmailTextView, EntersTextView,
            AllWishlistAddedTextView, AllBookedTextView, AllWasOnHandsTextView, RegsTextView,
            AuthsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        upgradeViews();
    }

    private void setViews() {
        view = requireView();
        FIOTextView = view.findViewById(R.id.fio);
        LoginTextView = view.findViewById(R.id.login);
        EmailTextView = view.findViewById(R.id.email);
        EntersTextView = view.findViewById(R.id.enters);
        AllWishlistAddedTextView = view.findViewById(R.id.all_wishlist_added);
        AllBookedTextView = view.findViewById(R.id.all_booked);
        AllWasOnHandsTextView = view.findViewById(R.id.all_was_on_hands);
        RegsTextView = view.findViewById(R.id.regs);
        AuthsTextView = view.findViewById(R.id.auths);
    }

    private void upgradeViews(){
        setFIO();
        setRegs();
        setAuths();
        setLogin();
        setEmail();
        setEnters();
    }
    private void setFIO(){
        String name = MainActivity.getSP().getString("name", "Нет данных");
        String surname = MainActivity.getSP().getString("surname", "");
        String result = getResources().getString(R.string.fio_stats) + " " + name + " " + surname;
        FIOTextView.setText(result);
    }

    private void setRegs(){
        int regs_num = MainActivity.getSP().getInt("regs_num", 0);
        String result = getResources().getString(R.string.all_regs) + " " + regs_num;
        RegsTextView.setText(result);
    }

    private void setAuths(){
        int auths_num = MainActivity.getSP().getInt("auths_num", 0);
        String result = getResources().getString(R.string.all_auths) + " " + auths_num;
        AuthsTextView.setText(result);
    }

    private void setLogin(){
        String login = MainActivity.getSP().getString("userid", "Нет данных");
        String regs_result = getResources().getString(R.string.login_stats) + " " + login;
        LoginTextView.setText(regs_result);
    }

    private void setEmail(){
        String email = MainActivity.getSP().getString("email", "Нет данных");
        String regs_result = getResources().getString(R.string.email_stats) + " " + email;
        EmailTextView.setText(regs_result);
    }

    private void setEnters(){
        int enters = MainActivity.getSP().getInt("enters", 0) + 1;
        String result = getResources().getString(R.string.app_executions) + " " + enters;
        EntersTextView.setText(result);
    }
}
