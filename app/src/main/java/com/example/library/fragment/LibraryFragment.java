package com.example.library.fragment;

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

public class LibraryFragment extends FragmentWithHeader {

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

    public static void setFragmentOnParent(Fragment fr) {
        fr.getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, getSelectedFragment()).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    private void setViews() {
        Button helpButton = getView().findViewById(R.id.help_button);
        Button registrationButton = getView().findViewById(R.id.registration_button);
        Button authorizationButton = getView().findViewById(R.id.authorization_button);
        registrationButton.setOnClickListener(regClickListener);
        authorizationButton.setOnClickListener(authClickListener);
        helpButton.setOnClickListener(helpClickListener);
    }
}