package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private ImageView upImage;
    private EditText upIndex, upName, upNumber;
    private ProgressBar progressBar;
    private Button deletBtn, upBtn;

    private Uri photoUri = null;
    private Uri downloadUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        progressBar = findViewById(R.id.up_progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        upImage = findViewById(R.id.up_photo);
        upIndex = findViewById(R.id.up_index_number);
        upName = findViewById(R.id.up_name);
        upNumber = findViewById(R.id.up_phone_number);
        upBtn = findViewById(R.id.up_btn);
        deletBtn = findViewById(R.id.deletBtn);

        String ID = getIntent().getStringExtra("USER_ID");

        final DocumentReference databaseRaf = database.collection("Sodesso_List").document(ID);
        databaseRaf.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(UpdateActivity.this, "Somthing Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null) {
                    //todo:  INDEX NAMES NUMBER ID URL Sodesso_List
                    //Pleceholder Request
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.ic_launcher_background);
                    Glide.with(UpdateActivity.this)
                            .setDefaultRequestOptions(requestOptions)
                            .load(documentSnapshot.getString("URL"))
                            .into(upImage);
                    upIndex.setText(documentSnapshot.getString("INDEX"));
                    upName.setText(documentSnapshot.getString("NAMES"));
                    upNumber.setText(documentSnapshot.getString("NUMBER"));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRaf.delete();
            }
        });

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        cropImage();

                    } else {
                        cropImage();
                    }

                } else {
                    cropImage();
                }
            }
        });

    }

    //Crop Activity Method
    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UpdateActivity.this);
    }

    public void updateInfo(View view) {
        //Gating ID
        final String ID = getIntent().getStringExtra("USER_ID");

        upBtn.setText("Updating Info...");
        upBtn.setClickable(false);


        progressBar.setVisibility(View.VISIBLE);
        String indexValue = upIndex.getText().toString();
        String nameValue = upName.getText().toString();
        String phoneValue = upNumber.getText().toString();

        //todo:  INDEX NAMES NUMBER ID URL
        Map<String, Object> values = new HashMap<>();
        values.put("INDEX", indexValue);
        values.put("NAMES", nameValue);
        values.put("NUMBER", phoneValue);

         DocumentReference databaseRef = database.collection("Sodesso_List").document("ID");
        databaseRef.update(values)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      Toast.makeText(UpdateActivity.this, "OK", Toast.LENGTH_SHORT).show();
                      progressBar.setVisibility(View.GONE);
                  }
              });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                upImage.setImageURI(photoUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
