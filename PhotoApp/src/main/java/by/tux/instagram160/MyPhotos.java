package by.tux.instagram160;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import by.tux.instagram160.threads.GetAllForUserThread;
import by.tux.instagram160.helpers.PhotoBody;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPhotos extends AppCompatActivity {

    private ListView listView;
    private int count = 0;
    List<PhotoBody> list;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photos);
        listView = findViewById(R.id.listView);

        list = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyPhotos.this);

        GetAllForUserThread getAllForUserThread = new GetAllForUserThread(sharedPreferences.getAll().get("login").toString());
        getAllForUserThread.start();
        while (getAllForUserThread.isAlive());
        list = getAllForUserThread.getList();

        count = list.size();

        listView.setAdapter(new CustomAdapter());
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.image_adapter, null);
            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView name = convertView.findViewById(R.id.name);
            TextView disc = convertView.findViewById(R.id.disc);

            name.setText(list.get(position).getName());
            disc.setText(list.get(position).getDisc());
            downloadBytes(list.get(position).getUrl(), imageView);

            return convertView;
        }
    }
    public void downloadBytes(String url, ImageView circleImageView){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        StorageReference storageReference1 = storageReference.child(url);

        long MAXBYTES = 10240*10240;
        storageReference1.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(bitmap);
            }
        });
    }
}