package by.tux.instagram160;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import by.tux.instagram160.threads.EditProfileThread;
import by.tux.instagram160.helpers.UserBody;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private ImageView EditActAvatar;
    private EditText name, lastName, disc;
    private Button save, clearAvatar;
    private String photoFileName = "";
    private boolean needUpdatePhoto = false;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    static final int GALLERY_REQUEST = 1; // return from image picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditActAvatar = findViewById(R.id.EditActAvatar);
        name = findViewById(R.id.name);
        lastName = findViewById(R.id.lastName);
        disc = findViewById(R.id.disc);
        save = findViewById(R.id.save);
        clearAvatar = findViewById(R.id.clearAvatar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);

        name.setText(sharedPreferences.getAll().get("name").toString());
        lastName.setText(sharedPreferences.getAll().get("lastName").toString());

        String mainPhotoValue = sharedPreferences.getAll().get("mainPhoto").toString();
//        if (!mainPhotoValue.equals(R.string.defaultAvatar)
//                || !mainPhotoValue.equals("") ){
        boolean needLoadFromFirebase = !mainPhotoValue.equals("")
                || !mainPhotoValue.equals(R.string.defaultAvatar);
        if ( needLoadFromFirebase ){
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference();
            StorageReference storageReferencePhoto = storageReference.child(sharedPreferences.getAll().get("mainPhoto").toString());

            long MAXBYTES = Long.MAX_VALUE;
            storageReferencePhoto.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    EditActAvatar.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    EditActAvatar.setImageResource(R.drawable.default_avatar);
                }
            });
        }
        else{
            EditActAvatar.setImageResource(R.drawable.default_avatar);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditActAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/**");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });
        clearAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFileName = getString(R.string.defaultAvatar);
                needUpdatePhoto = true;
                EditActAvatar.setImageResource(R.drawable.default_avatar);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //**************** Load image to Firebase
                if (needUpdatePhoto
                        && !photoFileName.equals(getString(R.string.defaultAvatar)) ){
                    if (hasImage(EditActAvatar) == false){
                        String message = "Нет изображения для загрузки";
                        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    }else{
                        Bitmap bitmap = ((BitmapDrawable) (EditActAvatar.getDrawable())).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 97, baos);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 97, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference storageReference1 = storageReference.child(photoFileName);
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
                    }
                }
                //****************
                UserBody userBody;
                if (!photoFileName.equals("") && needUpdatePhoto) {
                    userBody = UserBody.newBuilder()
                            .setLogin(sharedPreferences.getAll().get("login").toString())
//                            .setPassword(sharedPreferences.getAll().get("password").toString())
                            .setName(name.getText().toString())
                            .setLastName(lastName.getText().toString())
                            .setMainPhoto(photoFileName)
                            .build();
                }else {
                    userBody = UserBody.newBuilder()
                            .setLogin(sharedPreferences.getAll().get("login").toString())
                            .setName(name.getText().toString())
                            .setLastName(lastName.getText().toString())
                            .setMainPhoto(sharedPreferences.getAll().get("mainPhoto").toString())
                            .build();
                }
                editor.putString("name", name.getText().toString());
                editor.putString("lastName", lastName.getText().toString());
                if (needUpdatePhoto)
                    editor.putString("mainPhoto", photoFileName);
                editor.apply();

                EditProfileThread editProfileThread = new EditProfileThread(userBody);
                editProfileThread.start();

                while (editProfileThread.isAlive());

                Intent intent = new Intent(EditProfile.this, MainActivity.class);
                startActivity(intent);
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
                        EditActAvatar.setImageBitmap(bitmap);
                        if (hasImage(EditActAvatar)){
                            needUpdatePhoto = true;
                            photoFileName = String.valueOf(System.currentTimeMillis());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        EditActAvatar.setImageResource(R.drawable.default_avatar);
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