package com.example.library.fragment.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.helper.FragmentHelper;

public class LibraryFragment extends Fragment {

    private Button helpButton, registrationButton, authorizationButton, settingsButton, statisticsButton;

    private final View.OnClickListener regClickListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new RegistrationFragment());
    };
    private final View.OnClickListener authClickListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new AuthorizationFragment());
    };
    private final View.OnClickListener helpClickListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new HelpFragment());

    };
    private final View.OnClickListener settingsClickListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new SettingsFragment());
    };
    private final View.OnClickListener statisticsClickListener = view -> {
        new FragmentHelper((MainActivity) requireActivity(),
                false,true).execute(new StatisticsFragment());
    };

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