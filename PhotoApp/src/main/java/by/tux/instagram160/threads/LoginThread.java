package by.tux.instagram160.threads;

import static by.tux.instagram160.helpers.Constants.loginUrl;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import by.tux.instagram160.helpers.UserBody;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginThread extends Thread {

    private UserBody userBody;
    private UserBody userBodyFromServer;
    private String postResult = null;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public UserBody isIfSuccses() {
        return userBodyFromServer;
    }

    public LoginThread(UserBody userBody){
        this.userBody = userBody;
    }

    @Override
    public void run(){
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        OkHttpClient client = new OkHttpClient();
        try {
            Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
            RequestBody formBody = new FormBody.Builder(charset)
                    .add("login", userBody.getLogin())
                    .add("password", userBody.getPassword())
                    .build();
            Request request = new Request.Builder()
                .url(loginUrl)
                .post(formBody)
                .build();

            Response response = client.newCall(request).execute();
            postResult =  response.body().string();
        } catch (Exception e) {
            Log.d("Tag", e.toString());
            e.printStackTrace();
        }

        if (postResult!=null){
            if (!postResult.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(postResult);
                    userBodyFromServer = UserBody.newBuilder()
                        .setLogin(jsonObject.get("login").toString())
                        .setPassword(jsonObject.get("password").toString())
                        .setName(jsonObject.get("name").toString())
                        .setLastName(jsonObject.get("lastName").toString())
                        .setMainPhoto(jsonObject.get("mainPhoto").toString())
                        .build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
