package by.tux.instagram160.threads;

import static android.provider.Settings.Global.getString;
import static by.tux.instagram160.helpers.Constants.loginUrl;
import static by.tux.instagram160.helpers.Constants.registerUrl;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import by.tux.instagram160.R;
import by.tux.instagram160.helpers.UserBody;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegistrationThread extends Thread{

    private UserBody userBody;
    private boolean ifSuccses;
    private String postResult = null;

    public boolean isIfSuccses() {
        return ifSuccses;
    }

    public RegistrationThread(UserBody userBody){
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
                    .add("name", userBody.getName())
                    .add("lastName", userBody.getLastName())
                    .add("mainPhoto", "defaultAvatar")
                    .build();
            Request request = new Request.Builder()
                    .url(registerUrl)
                    .post(formBody)
                    .build();

            okhttp3.Response response = client.newCall(request).execute();
            ifSuccses = Boolean.parseBoolean(response.body().string());
            Log.e("if", String.valueOf(ifSuccses));
        } catch (Exception e) {
            Log.d("Tag", e.toString());
            e.printStackTrace();
        }
    }
}
