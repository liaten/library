package com.example.library.fragment.library;

import static com.example.library.MainActivity.getSelectedFragment;
import static com.example.library.MainActivity.setBottomNavigationViewUncheckable;
import static com.example.library.MainActivity.setSelectedFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.fragment.FragmentWithHeader;
import com.example.library.fragment.library.AuthorizationFragment;
import com.example.library.fragment.library.HelpFragment;
import com.example.library.fragment.library.RegistrationFragment;

public class LibraryFragment extends FragmentWithHeader {
    private Button helpButton, registrationButton, authorizationButton, settingsButton;
    private final View.OnClickListener regClickListener = view -> {
        setSelectedFragment(new RegistrationFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };
    private final View.OnClickListener authClickListener = view -> {
        setSelectedFragment(new AuthorizationFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };
    private final View.OnClickListener helpClickListener = view -> {
        setSelectedFragment(new HelpFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };
    private final View.OnClickListener settingsClickListener = view -> {
        setSelectedFragment(new SettingsFragment());
        setBottomNavigationViewUncheckable();
        setFragmentOnParent(this);
    };

    public static void setFragmentOnParent(@NonNull Fragment fr) {
        fr.getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, getSelectedFragment()).commit();
    }

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
    }
    private void setOnClickListeners(){
        registrationButton.setOnClickListener(regClickListener);
        authorizationButton.setOnClickListener(authClickListener);
        helpButton.setOnClickListener(helpClickListener);
        settingsButton.setOnClickListener(settingsClickListener);
    }
}