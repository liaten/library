package com.example.library.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CheckNetwork extends AsyncTask<Context, Void, Boolean>{

    //private static final String TAG = CheckNetwork.class.getSimpleName();
    public AsyncResponse delegate = null;

    @Override
    protected Boolean doInBackground(Context... contexts) {
        Context context = contexts[0];
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            //Log.d(TAG, "no internet connection");
            return false;
        } else {
//            if (info.isConnected()) {
//                Log.d(TAG, " internet connection available...");
//            } else {
//                Log.d(TAG, " internet connection");
//            }
            return pingCIT();
            //return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.processFinish(aBoolean);
    }

    private boolean pingCIT() {
        //Log.d(TAG, "executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 cit.rkomi.ru");
            int mExitValue = mIpAddrProcess.waitFor();
            //Log.d(TAG, " mExitValue " + mExitValue);
            return mExitValue == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}