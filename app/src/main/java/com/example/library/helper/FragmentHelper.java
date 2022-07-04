package com.example.library.helper;

import android.os.AsyncTask;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.library.MainActivity;
import com.example.library.R;
import com.example.library.fragment.home.HomeFragment;
import com.example.library.fragment.other.TopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentHelper extends AsyncTask<Fragment, Void, Void> {

    private static Fragment selectedFragment = new HomeFragment();
    private static final Fragment selectedTopFragment = new TopFragment();
    private static BottomNavigationView mainBottomNavigationView;
    private static MainActivity mainActivity;

    private static final String TAG = "FragmentHelper";

    public FragmentHelper(@NonNull MainActivity activity,
                          boolean checkBottomNavigation,
                          boolean topFragment) {
        mainActivity = activity;
        mainBottomNavigationView = MainActivity.getBottomNavigationView();
        setCheckNavigation(checkBottomNavigation);
        setTopFragment(topFragment);
    }

    public static void setCheckNavigation(boolean checkBottomNavigation) {
        if (checkBottomNavigation) {
            checkBottomNavigationView();
        } else {
            uncheckBottomNavigationView();
        }
    }

    public static void setTopFragment(boolean topFragment) {
        if (topFragment) {
            setSelectedTopFragmentToContainer();
        }
        else {
            mainActivity.findViewById(R.id.top_header_container).setVisibility(View.GONE);
        }
    }

    @NonNull
    public static Fragment getSelectedFragment() {
        return selectedFragment;
    }

    @NonNull
    public static Fragment getSelectedTopFragment() {
        return selectedTopFragment;
    }

    public static void setSelectedFragment(@NonNull Fragment fragment) {
        selectedFragment = fragment;
    }

    public static void uncheckBottomNavigationView() {
        Menu bottomMenu = mainBottomNavigationView.getMenu();
        bottomMenu.setGroupCheckable(0, false, true);
    }

    public static void checkBottomNavigationView() {
        Menu bottomMenu = mainBottomNavigationView.getMenu();
        bottomMenu.setGroupCheckable(0, true, true);
    }

    private static void setSelectedTopFragmentToContainer() {
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.top_header_container, selectedTopFragment).commit();
        mainActivity.findViewById(R.id.top_header_container).setVisibility(View.VISIBLE);
    }

    public void setSelectedFragmentToContainer() {
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    @NonNull
    @Override
    protected Void doInBackground(@NonNull Fragment... fragments) {
        setSelectedFragment(fragments[0]);
        return null;
    }

    @Override
    protected void onPostExecute(@NonNull Void unused) {
        super.onPostExecute(unused);
        setSelectedFragmentToContainer();
    }
}
