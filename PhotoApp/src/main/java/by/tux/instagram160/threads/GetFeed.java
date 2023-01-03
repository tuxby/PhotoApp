package by.tux.instagram160.threads;

import android.os.StrictMode;
import android.util.Log;

import by.tux.instagram160.helpers.Constants;
import by.tux.instagram160.helpers.PhotoBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetFeed extends Thread {
    private List<PhotoBody> list = new ArrayList<>();

    @Override
    public void run(){
        try {
            URL url = new URL(Constants.photosUrl);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String request = bufferedReader.readLine();
            JSONArray jsonArray = new JSONArray(request);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                PhotoBody photoBody = new PhotoBody(
                        jsonObject.get("disc").toString(),
                        jsonObject.get("url").toString(),
                        jsonObject.get("name").toString()
                );
                list.add(photoBody);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
