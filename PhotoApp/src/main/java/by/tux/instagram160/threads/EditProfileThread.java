package by.tux.instagram160.threads;

import android.os.StrictMode;
import android.util.Log;

import by.tux.instagram160.EditProfile;
import by.tux.instagram160.helpers.Constants;
import by.tux.instagram160.helpers.UserBody;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import by.tux.instagram160.helpers.UserBody;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileThread extends Thread{

    private UserBody userBody;

    public EditProfileThread(UserBody userBody){
        this.userBody = userBody;
    }

    @Override
    public void run(){
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());

        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody formBody = new FormBody.Builder(charset)
                    .add("name", userBody.getName())
                    .add("lastName", userBody.getLastName())
                    .add("mainPhoto", userBody.getMainPhoto())
                    .add("login", userBody.getLogin())
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.editUrl)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            Log.d("Tag", e.toString());
            e.printStackTrace();
        }
    }
}
