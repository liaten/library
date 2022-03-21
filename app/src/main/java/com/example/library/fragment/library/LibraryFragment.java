package com.example.library.fragment.library;

import static com.example.library.MainActivity.getBottomNavigationView;
import static com.example.library.helper.FragmentHelper.getSelectedFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.fragment.FragmentWithHeader;
import com.example.library.helper.FragmentHelper;

public class LibraryFragment extends FragmentWithHeader {

    private final View.OnClickListener regClickListener = view -> {
        new FragmentHelper((AppCompatActivity) requireActivity(),
                getBottomNavigationView(),
                "uncheck").execute(new RegistrationFragment());
    };
    private final View.OnClickListener authClickListener = view -> {
        new FragmentHelper((AppCompatActivity) requireActivity(),
                getBottomNavigationView(),
                "uncheck").execute(new AuthorizationFragment());
    };
    private final View.OnClickListener helpClickListener = view -> {
        new FragmentHelper((AppCompatActivity) requireActivity(),
                getBottomNavigationView(),
                "uncheck").execute(new HelpFragment());

    };
    private final View.OnClickListener settingsClickListener = view -> {
        new FragmentHelper((AppCompatActivity) requireActivity(),
                getBottomNavigationView(),
                "uncheck").execute(new SettingsFragment());
    };
    private final View.OnClickListener statisticsClickListener = view -> {
        new FragmentHelper((AppCompatActivity) requireActivity(),
                getBottomNavigationView(),
                "uncheck").execute(new StatisticsFragment());
    };
    private Button helpButton, registrationButton, authorizationButton, settingsButton, statisticsButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
        setOnClickListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    private void setViews() {
        helpButton = requireView().findViewById(R.id.help_button);
        registrationButton = requireView().findViewById(R.id.registration_button);
        authorizationButton = requireView().findViewById(R.id.authorization_button);
        settingsButton = requireView().findViewById(R.id.settings_button);
        statisticsButton = requireView().findViewById(R.id.statistics_button);
    }

    private void setOnClickListeners(){
        registrationButton.setOnClickListener(regClickListener);
        authorizationButton.setOnClickListener(authClickListener);
        helpButton.setOnClickListener(helpClickListener);
        settingsButton.setOnClickListener(settingsClickListener);
        statisticsButton.setOnClickListener(statisticsClickListener);
    }
}