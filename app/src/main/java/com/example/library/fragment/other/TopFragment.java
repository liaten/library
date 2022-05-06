package com.example.library.fragment.other;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.profile.ProfileFragment;
import com.example.library.helper.DateHelper;
import com.example.library.helper.FragmentHelper;
import com.example.library.helper.ImageDownloader;

import java.net.MalformedURLException;
import java.net.URL;

public class TopFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_header, container, false);
    }

    private ImageView profileImageView;
    private CardView profileCardView;
    private View view;

    @NonNull
    public View.OnClickListener profileClickListener = view -> new FragmentHelper((MainActivity) requireActivity(),
            false,true).execute(new ProfileFragment());

    public static void updateDate() {
        String date_url = "https://yandex.ru/time/sync.json?geo=213";
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
        setOnClickListeners();
        setImageOnProfile();
    }

    private void setViews() {
        view = requireView();
        profileCardView = view.findViewById(R.id.profile_card_view);
        profileImageView = view.findViewById(R.id.profileImageView);

    }
    private void setOnClickListeners(){
        profileCardView.setOnClickListener(profileClickListener);
        profileImageView.setOnClickListener(profileClickListener);
    }

    private void setImageOnProfile() {
        new ImageDownloader(profileImageView).execute("https://liaten.ru/pictures_small/m.jpg");
    }
}
