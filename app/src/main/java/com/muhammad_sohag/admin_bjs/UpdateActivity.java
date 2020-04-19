package com.muhammad_sohag.admin_bjs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CircleImageView upImage;
    private EditText upName, upNumber, upNumber2;
    private ProgressBar progressBar;
    private Button deletBtn, upBtn;
    private Spinner bloodGroupSpinner;
    private String[] bloodGroups;
    private TextView blood;

    private Uri imageUri = null;
    private Uri downloadUri = null;
    private boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.up_progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        upImage = findViewById(R.id.up_photo);
        upName = findViewById(R.id.up_name);
        upNumber = findViewById(R.id.up_phone_number);
        upNumber2 = findViewById(R.id.up_phone_number2);
        upBtn = findViewById(R.id.up_btn);
        deletBtn = findViewById(R.id.deletBtn);
        blood = findViewById(R.id.up_blood);
        bloodGroupSpinner = findViewById(R.id.up_blood_group);

        String ID = getIntent().getStringExtra("uid");

        final DocumentReference databaseRaf = database.collection("Sodesso_List").document(ID);
        databaseRaf.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(UpdateActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null) {
                    //Placeholder Request
//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.placeholder(R.drawable.ic_launcher_background);
//                    Glide.with(UpdateActivity.this)
//                            .setDefaultRequestOptions(requestOptions)
//                            .load(documentSnapshot.getString("url"))
//                            .into(upImage);

                    upName.setText(documentSnapshot.getString("name"));
                    upNumber.setText(documentSnapshot.getString("number"));
                    upNumber2.setText(documentSnapshot.getString("number2"));
                    blood.setText(documentSnapshot.getString("blood"));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRaf.delete().addOnCompleteListener(UpdateActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });


        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, bloodGroups);
        bloodGroupSpinner.setAdapter(bloodAdapter);

    }





    public void updateInfo(View view) {
        progressBar.setVisibility(View.VISIBLE);
        //Gating ID
        final String ID = getIntent().getStringExtra("uid");

        upBtn.setText("Updating Info...");
        upBtn.setClickable(false);


        progressBar.setVisibility(View.VISIBLE);

        String nameValue = upName.getText().toString();
        String phoneValue = upNumber.getText().toString();


        Map<String, Object> values = new HashMap<>();
        values.put("name", nameValue);
        values.put("number", phoneValue);

        final DocumentReference databaseRef = database.collection("Sodesso_List").document(ID);
        databaseRef.update(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            if (isChange) {
                                final StorageReference imagePath = storageReference.child("Profile_Images").child(ID + ".jpeg");
                                imagePath.putFile(imageUri)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            downloadUri = uri;
                                                            if (downloadUri != null) {
                                                                Map<String, Object> uValue = new HashMap<>();
                                                                uValue.put("url", downloadUri.toString());
                                                                databaseRef.update(uValue)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                upBtn.setText("Updated");
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(UpdateActivity.this, "Upload Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UpdateActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    }
                });


    }


    //Upload Profile Image
    private void selectImage() {
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


    //Crop Activity Method
    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .start(UpdateActivity.this);
    }


    //Image Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Toast.makeText(this, imageUri + "", Toast.LENGTH_SHORT).show();
                upImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
