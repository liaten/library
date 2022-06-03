package com.example.library.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class CheckNetwork extends AsyncTask<Context, Void, Boolean>{

//    private static final String TAG = CheckNetwork.class.getSimpleName();
    public AsyncResponse delegate = null;

    public CheckNetwork(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Context... contexts) {
        Context context = contexts[0];
        NetworkInfo info = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
//            Log.d(TAG, "no internet connection");
            return false;
        } else {
            if (info.isConnected()) {
//                Log.d(TAG, " internet connection available...");
            } else {
//                Log.d(TAG, " internet connection");
            }
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        delegate.processFinish(aBoolean);
    }
}