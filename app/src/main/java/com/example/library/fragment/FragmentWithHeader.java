package com.example.library.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.profile.ProfileFragment;
import com.example.library.helper.DateHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;

import java.net.MalformedURLException;
import java.net.URL;

public class FragmentWithHeader extends Fragment {
    private ImageView profileImageView;
    @NonNull
    public View.OnClickListener profileClickListener = view -> new FragmentHelper((MainActivity) requireActivity(),
            false,true).execute(new ProfileFragment());

    public static void updateDate() {
        String date_url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Europe/Moscow";
        DateHelper dateHelper = new DateHelper();
        try {
            URL url = new URL(date_url);
            dateHelper.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateDate();
        setViews();
        setImageOnProfile();
    }

    private void setViews() {
        profileImageView = requireView().findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(profileClickListener);
    }

    private void setImageOnProfile() {
        new ImageDownloader(profileImageView).execute("https://liaten.ru/pictures/m.jpg");
    }
}
