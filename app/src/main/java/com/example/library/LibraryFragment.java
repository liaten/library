package com.example.library;

import static com.example.library.HomeFragment.updateDate;
import static com.example.library.MainActivity.getSelectedFragment;
import static com.example.library.MainActivity.setSelectedFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LibraryFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDate();
        setViews();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }
    private void setViews(){
        Button helpButton = getView().findViewById(R.id.help_button);
        Button registrationButton = getView().findViewById(R.id.registration_button);
        Button authorizationButton = getView().findViewById(R.id.authorization_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedFragment(new RegistrationFragment());
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, getSelectedFragment()).commit();
            }
        });
    }
}