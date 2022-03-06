package com.example.library.fragment;

import static com.example.library.MainActivity.setSelectedFragment;
import static com.example.library.fragment.LibraryFragment.setFragmentOnParent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.helper.DateHelper;

import java.net.MalformedURLException;
import java.net.URL;

public class FragmentWithHeader extends Fragment {

    public View.OnClickListener profileClickListener = view -> {
        setSelectedFragment(new ProfileFragment());
        setFragmentOnParent(this);
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
    }

    private void setViews() {
        ImageView profileImageView = getView().findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(profileClickListener);
    }
}
