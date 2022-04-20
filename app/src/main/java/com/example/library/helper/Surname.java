package com.example.library.helper;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Surname extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;
    private static final String TAG = "Username";
    public Surname(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected String doInBackground(String... strings) {
        try{
            String userid = strings[0];
            String link = "https://liaten.ru/db/surname.php?userid="+userid;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        } catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.returnSurname(s);
    }
}
