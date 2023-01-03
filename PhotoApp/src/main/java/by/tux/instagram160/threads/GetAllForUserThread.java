package by.tux.instagram160.threads;

import android.os.StrictMode;

import by.tux.instagram160.helpers.Constants;
import by.tux.instagram160.helpers.PhotoBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import by.tux.instagram160.helpers.PhotoBody;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetAllForUserThread extends Thread {
    private String login;
    private List<PhotoBody> list = new ArrayList<PhotoBody>();

    public GetAllForUserThread(String login) {
        this.login = login;
    }

    public List<PhotoBody> getList() {
        return list;
    }

    @Override
    public void run() {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());

        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody formBody = new FormBody.Builder(charset)
                    .add("login", login)
                    .build();
            Request request = new Request.Builder()
                    .url(Constants.photosUser)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String postResult = response.body().string();
            JSONArray jsonArray = new JSONArray(postResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                PhotoBody photoBody = new PhotoBody(
                        jsonObject.get("disc").toString(),
                        jsonObject.get("url").toString(),
                        jsonObject.get("authorId").toString()
                );
                list.add(photoBody);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
