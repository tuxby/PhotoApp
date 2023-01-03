package by.tux.instagram160;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import by.tux.instagram160.threads.AddPhotoThread;
import by.tux.instagram160.helpers.PhotoBody;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddPhoto extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1; // return from image picker

    private ImageView imageViewAddPhotoAct;
    private TextView nameAddPhotoAct;
    private Button buttonAddPhotoAct;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        imageViewAddPhotoAct = findViewById(R.id.imageViewAddPhotoAct);
        imageViewAddPhotoAct.setImageResource(R.drawable.default_image);
        buttonAddPhotoAct = findViewById(R.id.buttonAddPhotoAct);
        nameAddPhotoAct = findViewById(R.id.nameAddPhotoAct);

        imageViewAddPhotoAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/**");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });
        buttonAddPhotoAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUrl = String.valueOf(System.currentTimeMillis());
                Bitmap bitmap = ((BitmapDrawable) (imageViewAddPhotoAct.getDrawable())).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 97, byteArrayOutputStream);
                bitmap.compress(Bitmap.CompressFormat.PNG, 97, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference storageReference1 = storageReference.child(photoUrl);
                UploadTask uploadTask = storageReference1.putBytes(bytes);
                Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return storageReference1.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String message = task.getResult().toString();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                message, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddPhoto.this);
                PhotoBody photoBody = new PhotoBody("",photoUrl,nameAddPhotoAct.getText().toString());

                AddPhotoThread addPhotoThread = new AddPhotoThread(photoBody, sharedPreferences.getAll()
                        .get("login").toString());

                addPhotoThread.start();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bitmap = null;
        switch(requestCode ) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK && imageReturnedIntent!=null){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageViewAddPhotoAct.setImageBitmap(bitmap);
                        if (!hasImage(imageViewAddPhotoAct)){
                            String message = "Ошибка загрузки изображения";
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        imageViewAddPhotoAct.setImageResource(R.drawable.default_image);
                    }
                }
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
}