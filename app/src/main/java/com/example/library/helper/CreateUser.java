package com.example.library.helper;

import static com.example.library.helper.NetworkHelper.getJSONFromURL;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

public class CreateUser extends AsyncTask<String, Void, JSONObject> {

    public AsyncResponse delegate;
    public CreateUser(AsyncResponse delegate){
        this.delegate = delegate;
    }
    @Override
    protected JSONObject doInBackground(String... strings) {
        try{
            String surname = strings[0];
            String name = strings[1];
            String patronymic = strings[2];
            String phone = strings[3];
            String date = strings[4];
            String email = strings[5];
            String userid = strings[6];
            String password = strings[7];
            String gender = strings[8];
            String link = "https://liaten.ru/db/create_user.php" +
                    "?surname=" + surname +
                    "&name=" + name +
                    "&patronymic=" + patronymic +
                    "&phone=" + phone +
                    "&date=" + date +
                    "&email=" + email +
                    "&userid=" + userid +
                    "&password=" + password +
                    "&gender=" + gender;
            URL url = new URL(link);
            return new JSONObject(getJSONFromURL(url));
        } catch(Exception e){
//            e.printStackTrace();
            return new JSONObject();
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.returnJSONObject(jsonObject);
    }
}
