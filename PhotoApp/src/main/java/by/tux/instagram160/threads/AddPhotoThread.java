package by.tux.instagram160.threads;

import android.os.StrictMode;
import android.util.Log;

import by.tux.instagram160.helpers.Constants;
import by.tux.instagram160.helpers.PhotoBody;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPhotoThread extends Thread{

    private PhotoBody photoBody;
    private String login;

    public AddPhotoThread(PhotoBody photoBody, String login){
        this.photoBody = photoBody;
        this.login = login;
    }
    @Override
    public void run(){
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());

        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody formBody = new FormBody.Builder(charset)
                    .add("disc", photoBody.getName())
                    .add("url", photoBody.getUrl())
                    .add("login", login)
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.addPhoto)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            Log.d("Tag", e.toString());
            e.printStackTrace();
        }
    }
}
