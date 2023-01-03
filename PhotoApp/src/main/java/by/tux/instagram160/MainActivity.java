package by.tux.instagram160;

import static by.tux.instagram160.helpers.Constants.getFirebaseImageFullURL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView name, disc;
    private Button editProfile, myPhotos, othersPhotos, addPhoto;
    private CircleImageView circleImageView;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (sharedPreferences.getAll().get("name")==null){
            Intent intent = new Intent(MainActivity.this, ChooseType.class);
            startActivity(intent);
            finish();
        }else {
            name = findViewById(R.id.name);
            disc = findViewById(R.id.disc);

            editProfile = findViewById(R.id.editProfile);
            myPhotos = findViewById(R.id.myPhotos);
            othersPhotos = findViewById(R.id.othersPhotos);
            addPhoto = findViewById(R.id.addPhoto);

            circleImageView = findViewById(R.id.circleImageView);

            name.setText(sharedPreferences.getAll().get("name") + " " + sharedPreferences.getAll().get("lastName"));

            if (sharedPreferences.getAll().get("mainPhoto")!= null
                    && !sharedPreferences.getAll().get("mainPhoto").equals("")) {
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference();
                StorageReference storageReferencePhoto = storageReference.child(sharedPreferences.getAll().get("mainPhoto").toString());

                long MAXBYTES = Long.MAX_VALUE;
                storageReferencePhoto.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        circleImageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        circleImageView.setImageResource(R.drawable.default_avatar);
                    }
                });
            }else{
                circleImageView.setImageResource(R.drawable.default_avatar);
            }

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sharedPreferences.getAll().get("mainPhoto") != null
                            && !sharedPreferences.getAll().get("mainPhoto").toString().equals("")) {
                        firebaseStorage = FirebaseStorage.getInstance();
                        storageReference = firebaseStorage.getReference();
                        StorageReference storageReferencePhoto = storageReference.child(sharedPreferences.getAll().get("mainPhoto").toString());

                        long MAXBYTES = Long.MAX_VALUE;
                        storageReferencePhoto.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                circleImageView.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                circleImageView.setImageResource(R.drawable.default_avatar);
                            }
                        });
                    } else {
                        circleImageView.setImageResource(R.drawable.default_avatar);
                    }
                }
            });

            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, EditProfile.class);
                    startActivity(intent);
                }
            });

            myPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyPhotos.class);
                    startActivity(intent);
                }
            });

            addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddPhoto.class);
                    startActivity(intent);
                }
            });

            othersPhotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                    startActivity(intent);
                }
            });
        }
    }



    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        name.setText(sharedPreferences.getAll().get("name") + " " + sharedPreferences.getAll().get("lastName"));

        if (sharedPreferences.getAll().get("mainPhoto")!= null
                && !sharedPreferences.getAll().get("mainPhoto").equals("")) {
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference();
            StorageReference storageReferencePhoto = storageReference.child(sharedPreferences.getAll().get("mainPhoto").toString());

            long MAXBYTES = Long.MAX_VALUE;
            storageReferencePhoto.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {

                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    circleImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    circleImageView.setImageResource(R.drawable.default_avatar);
                }
            });
        }else{
            circleImageView.setImageResource(R.drawable.default_avatar);
        }
    }
}