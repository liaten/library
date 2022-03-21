package com.example.library.helper;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.library.R;
import com.example.library.fragment.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressLint("StaticFieldLeak")
public class FragmentHelper extends AsyncTask<Fragment, Void, Void> {

    private static Fragment selectedFragment = new HomeFragment();
    private static BottomNavigationView mainBottomNavigationView;
    private static AppCompatActivity mainActivity;

    public FragmentHelper(@NonNull AppCompatActivity appCompatActivity,
                          @Nullable BottomNavigationView bottomNavigationView,
                          @NonNull String checkBottomNavigation) {
        mainActivity = appCompatActivity;
        mainBottomNavigationView = bottomNavigationView;
        setCheckNavigation(checkBottomNavigation);
    }

    public static void setCheckNavigation(@NonNull String checkBottomNavigation){
        switch (checkBottomNavigation){
            case "check":
                checkBottomNavigationView();
                break;
            case "uncheck":
                uncheckBottomNavigationView();
                break;
        }
    }

    @NonNull
    public static Fragment getSelectedFragment() {
        return selectedFragment;
    }

    public static void setSelectedFragment(@NonNull Fragment fragment) {
        selectedFragment = fragment;
    }

    public static void uncheckBottomNavigationView() {
        mainBottomNavigationView.getMenu().setGroupCheckable(0, false, true);
    }

    public static void checkBottomNavigationView() {
        mainBottomNavigationView.getMenu().setGroupCheckable(0, true, true);
    }

    public void setSelectedFragmentToContainer() {
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @NonNull
    @Override
    protected Void doInBackground(@Nullable Fragment... fragments) {
        Fragment fragment = null;
        if (fragments != null) {
            fragment = fragments[0];
            setSelectedFragment(fragment);
        }
        return null;
    }

    @Override
    protected void onPostExecute(@NonNull Void unused) {
        super.onPostExecute(unused);
        setSelectedFragmentToContainer();
    }
}
