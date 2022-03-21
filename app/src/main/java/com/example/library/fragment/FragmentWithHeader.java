package com.example.library.fragment;

import static com.example.library.MainActivity.getBottomNavigationView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    public View.OnClickListener profileClickListener = view -> {
        new FragmentHelper((AppCompatActivity) getActivity(),
                getBottomNavigationView(),
                "check").execute(new ProfileFragment());
    };

    public static void updateDate() {
        try {
            String date_url = "https://www.timeapi.io/api/Time/current/zone?timeZone=Europe/Moscow";
            URL url = new URL(date_url);
            DateHelper dateHelper = new DateHelper();
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
