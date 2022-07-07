package com.example.library.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.library.response.NetworkResponse;

public class CheckNetwork extends AsyncTask<Context, Void, Boolean>{

//    private static final String TAG = CheckNetwork.class.getSimpleName();
    public NetworkResponse delegate;

    public CheckNetwork(NetworkResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(Context... contexts) {
        NetworkInfo info = ((ConnectivityManager)
                contexts[0].getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    protected void onPostExecute(Boolean networkState) {
        super.onPostExecute(networkState);
        delegate.NetworkCheckFinish(networkState);
    }
}